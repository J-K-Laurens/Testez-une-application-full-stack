describe('Sessions - Gestion des sessions', () => {

  // ==================== LISTE DES SESSIONS ====================
  describe('Liste des sessions', () => {

    it('Devrait afficher la liste des sessions', () => {
      loginAsAdmin();
      cy.url().should('include', '/sessions');
      cy.contains('Rentals available').should('be.visible');
      cy.contains('Yoga matinal').should('be.visible');
      cy.contains('Yoga relaxation').should('be.visible');
    });

    it('Devrait afficher le bouton Create pour un admin', () => {
      loginAsAdmin();
      cy.contains('button', 'Create').should('be.visible');
    });

    it('Ne devrait PAS afficher le bouton Create pour un utilisateur normal', () => {
      loginAsUser();
      cy.contains('button', 'Create').should('not.exist');
    });

    it('Devrait afficher les boutons Detail et Edit pour un admin', () => {
      loginAsAdmin();
      cy.contains('button', 'Detail').should('be.visible');
      cy.contains('button', 'Edit').should('be.visible');
    });

    it('Devrait afficher le bouton Detail mais PAS Edit pour un utilisateur normal', () => {
      loginAsUser();
      cy.contains('button', 'Detail').should('be.visible');
      cy.contains('button', 'Edit').should('not.exist');
    });

    it('Devrait gérer une liste vide de sessions', () => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: adminUser }).as('login');
      cy.intercept('GET', '/api/session', []).as('getEmptySessions');
      cy.get('input[formControlName=email]').type('admin@studio.com');
      cy.get('input[formControlName=password]').type('password123{enter}{enter}');
      cy.wait('@login');
      cy.wait('@getEmptySessions');
      cy.url().should('include', '/sessions');
      cy.contains('Rentals available').should('be.visible');
    });

  });


  // ==================== CRÉATION DE SESSION ====================
  describe('Création de session (Admin)', () => {

    beforeEach(() => {
      loginAsAdmin();
    });

    it('Devrait naviguer vers le formulaire de création', () => {
      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.contains('button', 'Create').click();
      cy.wait('@getTeachers');
      cy.url().should('include', '/sessions/create');
      cy.contains('Create session').should('be.visible');
    });

    it('Devrait créer une session avec succès', () => {
      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.intercept('POST', '/api/session', {
        statusCode: 200,
        body: { id: 3, name: 'Nouvelle session', description: 'Description test', date: '2024-03-01', teacher_id: 1, users: [] }
      }).as('createSession');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');

      cy.contains('button', 'Create').click();
      cy.wait('@getTeachers');

      cy.get('input[formControlName=name]').type('Nouvelle session');
      cy.get('input[formControlName=date]').type('2024-03-01');
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('mat-option').contains('Jean Dupont').click();
      cy.get('textarea[formControlName=description]').type('Description de la nouvelle session');

      cy.get('button[type=submit]').click();
      cy.wait('@createSession');

      cy.url().should('include', '/sessions');
      cy.contains('Session created').should('be.visible');
    });

    it('Devrait désactiver le bouton Save si le formulaire est invalide', () => {
      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.contains('button', 'Create').click();
      cy.wait('@getTeachers');

      cy.get('button[type=submit]').should('be.disabled');

      cy.get('input[formControlName=name]').type('Test');
      cy.get('button[type=submit]').should('be.disabled');
    });

    it('Devrait permettre de revenir à la liste via le bouton retour', () => {
      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
      cy.contains('button', 'Create').click();
      cy.wait('@getTeachers');

      cy.get('button[routerLink="/sessions"]').click();
      cy.url().should('include', '/sessions');
      cy.url().should('not.include', '/create');
    });

  });


  // ==================== MODIFICATION DE SESSION ====================
  describe('Modification de session (Admin)', () => {

    beforeEach(() => {
      loginAsAdmin();
    });

    it('Devrait naviguer vers le formulaire de modification', () => {
      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.intercept('GET', '/api/session/1', mockSessions[0]).as('getSession');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Edit').click();
      });

      cy.wait('@getTeachers');
      cy.wait('@getSession');
      cy.url().should('include', '/sessions/update/1');
      cy.contains('Update session').should('be.visible');
    });

    it('Devrait pré-remplir le formulaire avec les données existantes', () => {
      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.intercept('GET', '/api/session/1', mockSessions[0]).as('getSession');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Edit').click();
      });

      cy.wait('@getTeachers');
      cy.wait('@getSession');

      cy.get('input[formControlName=name]').should('have.value', 'Yoga matinal');
      cy.get('textarea[formControlName=description]').should('have.value', 'Session de yoga pour bien commencer la journée');
    });

    it('Devrait modifier une session avec succès', () => {
      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.intercept('GET', '/api/session/1', mockSessions[0]).as('getSession');
      cy.intercept('PUT', '/api/session/1', {
        statusCode: 200,
        body: { ...mockSessions[0], name: 'Yoga matinal modifié' }
      }).as('updateSession');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Edit').click();
      });

      cy.wait('@getTeachers');
      cy.wait('@getSession');

      cy.get('input[formControlName=name]').clear().type('Yoga matinal modifié');
      cy.get('button[type=submit]').click();

      cy.wait('@updateSession');
      cy.url().should('include', '/sessions');
      cy.contains('Session updated').should('be.visible');
    });

  });


  // ==================== DÉTAIL DE SESSION ====================
  describe('Détail de session', () => {

    it('Devrait afficher les détails d\'une session (Admin)', () => {
      loginAsAdmin();

      cy.intercept('GET', '/api/session/1', mockSessions[0]).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.url().should('include', '/sessions/detail/1');
      cy.contains('Yoga Matinal').should('be.visible');
      cy.contains('Jean DUPONT').should('be.visible');
      cy.contains('2 attendees').should('be.visible');
      cy.contains('Session de yoga pour bien commencer la journée').should('be.visible');
    });

    it('Devrait afficher le bouton Delete pour un admin', () => {
      loginAsAdmin();

      cy.intercept('GET', '/api/session/1', mockSessions[0]).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.contains('button', 'Delete').should('be.visible');
    });

    it('Devrait afficher le bouton Participate pour un utilisateur non inscrit', () => {
      loginAsUser();

      const sessionWithoutUser = { ...mockSessions[0], users: [3, 4] }; // User 2 n'est pas inscrit
      cy.intercept('GET', '/api/session/1', sessionWithoutUser).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.contains('button', 'Participate').should('be.visible');
      cy.contains('button', 'Delete').should('not.exist');
    });

    it('Devrait afficher le bouton Do not participate pour un utilisateur inscrit', () => {
      loginAsUser();

      const sessionWithUser = { ...mockSessions[0], users: [2, 3] }; // User 2 est inscrit
      cy.intercept('GET', '/api/session/1', sessionWithUser).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.contains('button', 'Do not participate').should('be.visible');
    });

  });


  // ==================== PARTICIPATION ====================
  describe('Participation aux sessions', () => {

    it('Devrait permettre de participer à une session', () => {
      loginAsUser();

      const sessionWithoutUser = { ...mockSessions[0], users: [3, 4] };
      const sessionWithUser = { ...mockSessions[0], users: [2, 3, 4] };

      cy.intercept('GET', '/api/session/1', sessionWithoutUser).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.intercept('POST', '/api/session/1/participate/2', { statusCode: 200 }).as('participate');
      cy.intercept('GET', '/api/session/1', sessionWithUser).as('getSessionUpdated');

      cy.contains('button', 'Participate').click();
      cy.wait('@participate');
      cy.wait('@getSessionUpdated');

      cy.contains('button', 'Do not participate').should('be.visible');
    });

    it('Devrait permettre de se désinscrire d\'une session', () => {
      loginAsUser();

      const sessionWithUser = { ...mockSessions[0], users: [2, 3] };
      const sessionWithoutUser = { ...mockSessions[0], users: [3] };

      cy.intercept('GET', '/api/session/1', sessionWithUser).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.intercept('DELETE', '/api/session/1/participate/2', { statusCode: 200 }).as('unparticipate');
      cy.intercept('GET', '/api/session/1', sessionWithoutUser).as('getSessionUpdated');

      cy.contains('button', 'Do not participate').click();
      cy.wait('@unparticipate');
      cy.wait('@getSessionUpdated');

      cy.contains('button', 'Participate').should('be.visible');
    });

  });


  // ==================== SUPPRESSION DE SESSION ====================
  describe('Suppression de session (Admin)', () => {

    it('Devrait supprimer une session avec succès', () => {
      loginAsAdmin();

      cy.intercept('GET', '/api/session/1', mockSessions[0]).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.intercept('DELETE', '/api/session/1', { statusCode: 200 }).as('deleteSession');
      cy.intercept('GET', '/api/session', [mockSessions[1]]).as('getSessions');

      cy.contains('button', 'Delete').click();
      cy.wait('@deleteSession');

      cy.url().should('include', '/sessions');
      cy.contains('Session deleted').should('be.visible');
    });

  });


  // ==================== NAVIGATION ====================
  describe('Navigation', () => {

    it('Devrait permettre de revenir en arrière depuis le détail', () => {
      loginAsAdmin();

      cy.intercept('GET', '/api/session/1', mockSessions[0]).as('getSession');
      cy.intercept('GET', '/api/teacher/1', mockTeachers[0]).as('getTeacher');

      cy.contains('.item', 'Yoga matinal').within(() => {
        cy.contains('button', 'Detail').click();
      });

      cy.wait('@getSession');
      cy.wait('@getTeacher');

      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
      cy.get('button').find('mat-icon').contains('arrow_back').click();

      cy.url().should('include', '/sessions');
    });

  });


  // ==================== GESTION DES ERREURS ====================
  describe('Gestion des erreurs', () => {

    it('Devrait gérer une erreur lors du chargement des sessions', () => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: adminUser }).as('login');
      cy.intercept('GET', '/api/session', { statusCode: 500, body: { message: 'Server Error' } }).as('getSessionsError');

      cy.get('input[formControlName=email]').type('admin@studio.com');
      cy.get('input[formControlName=password]').type('password123{enter}{enter}');

      cy.wait('@login');
      cy.wait('@getSessionsError');
    });

    it('Devrait gérer une erreur lors de la création de session', () => {
      loginAsAdmin();

      cy.intercept('GET', '/api/teacher', mockTeachers).as('getTeachers');
      cy.intercept('POST', '/api/session', { statusCode: 500, body: { message: 'Server Error' } }).as('createError');

      cy.contains('button', 'Create').click();
      cy.wait('@getTeachers');

      cy.get('input[formControlName=name]').type('Test session');
      cy.get('input[formControlName=date]').type('2024-03-01');
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('mat-option').contains('Jean Dupont').click();
      cy.get('textarea[formControlName=description]').type('Description test');

      cy.get('button[type=submit]').click();
      cy.wait('@createError');
    });

  });

});

