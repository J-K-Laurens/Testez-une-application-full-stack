describe('Me - Page profil utilisateur', () => {

  // ==================== DONNÉES DE TEST ====================
  const adminUser = {
    id: 1,
    email: 'admin@studio.com',
    firstName: 'Admin',
    lastName: 'User',
    admin: true,
    createdAt: '2024-01-01',
    updatedAt: '2024-01-15'
  };

  const regularUser = {
    id: 2,
    email: 'user@studio.com',
    firstName: 'Regular',
    lastName: 'User',
    admin: false,
    createdAt: '2024-01-05',
    updatedAt: '2024-01-20'
  };

  const mockSessions = [];


  // ==================== HELPER : LOGIN ====================
  const loginAsAdmin = () => {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: { id: 1, username: 'admin@studio.com', firstName: 'Admin', lastName: 'User', admin: true }
    }).as('loginAdmin');
    cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
    cy.get('input[formControlName=email]').type('admin@studio.com');
    cy.get('input[formControlName=password]').type('password123{enter}{enter}');
    cy.wait('@loginAdmin');
    cy.wait('@getSessions');
  };

  const loginAsUser = () => {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: { id: 2, username: 'user@studio.com', firstName: 'Regular', lastName: 'User', admin: false }
    }).as('loginUser');
    cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
    cy.get('input[formControlName=email]').type('user@studio.com');
    cy.get('input[formControlName=password]').type('password123{enter}{enter}');
    cy.wait('@loginUser');
    cy.wait('@getSessions');
  };


  // ==================== AFFICHAGE PROFIL ADMIN ====================
  describe('Profil Admin', () => {

    it('Devrait afficher les informations de l\'admin', () => {
      loginAsAdmin();

      cy.intercept('GET', '/api/user/1', adminUser).as('getUser');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.url().should('include', '/me');
      cy.contains('User information').should('be.visible');
      cy.contains('Admin USER').should('be.visible');
      cy.contains('admin@studio.com').should('be.visible');
      cy.contains('You are admin').should('be.visible');
    });

    it('Ne devrait PAS afficher le bouton Delete pour un admin', () => {
      loginAsAdmin();

      cy.intercept('GET', '/api/user/1', adminUser).as('getUser');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.contains('Delete my account').should('not.exist');
      cy.contains('button', 'Detail').should('not.exist');
    });

  });


  // ==================== AFFICHAGE PROFIL UTILISATEUR ====================
  describe('Profil Utilisateur normal', () => {

    it('Devrait afficher les informations de l\'utilisateur', () => {
      loginAsUser();

      cy.intercept('GET', '/api/user/2', regularUser).as('getUser');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.url().should('include', '/me');
      cy.contains('User information').should('be.visible');
      cy.contains('Regular USER').should('be.visible');
      cy.contains('user@studio.com').should('be.visible');
      cy.contains('You are admin').should('not.exist');
    });

    it('Devrait afficher le bouton Delete pour un utilisateur normal', () => {
      loginAsUser();

      cy.intercept('GET', '/api/user/2', regularUser).as('getUser');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.contains('Delete my account').should('be.visible');
    });

    it('Devrait afficher les dates de création et mise à jour', () => {
      loginAsUser();

      cy.intercept('GET', '/api/user/2', regularUser).as('getUser');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.contains('Create at:').should('be.visible');
      cy.contains('Last update:').should('be.visible');
    });

  });


  // ==================== SUPPRESSION DE COMPTE ====================
  describe('Suppression de compte', () => {

    it('Devrait supprimer le compte et rediriger vers l\'accueil', () => {
      loginAsUser();

      cy.intercept('GET', '/api/user/2', regularUser).as('getUser');
      cy.intercept('DELETE', '/api/user/2', { statusCode: 200 }).as('deleteUser');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.contains('button', 'Detail').click();
      cy.wait('@deleteUser');

      cy.contains('Your account has been deleted').should('be.visible');
      cy.url().should('include', '/');
      cy.contains('span', 'Login').should('be.visible');
    });

    it('Devrait gérer une erreur lors de la suppression', () => {
      loginAsUser();

      cy.intercept('GET', '/api/user/2', regularUser).as('getUser');
      cy.intercept('DELETE', '/api/user/2', { statusCode: 500, body: { message: 'Server Error' } }).as('deleteError');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.contains('button', 'Detail').click();
      cy.wait('@deleteError');
    });

  });


  // ==================== NAVIGATION ====================
  describe('Navigation', () => {

    it('Devrait permettre de revenir en arrière', () => {
      loginAsUser();

      cy.intercept('GET', '/api/user/2', regularUser).as('getUser');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');

      cy.contains('span', 'Account').click();
      cy.wait('@getUser');

      cy.get('button').find('mat-icon').contains('arrow_back').click();

      cy.url().should('include', '/sessions');
    });

  });


  // ==================== GESTION DES ERREURS ====================
  describe('Gestion des erreurs', () => {

    it('Devrait gérer une erreur lors du chargement du profil', () => {
      loginAsUser();

      cy.intercept('GET', '/api/user/2', { statusCode: 500, body: { message: 'Server Error' } }).as('getUserError');

      cy.contains('span', 'Account').click();
      cy.wait('@getUserError');

      cy.url().should('include', '/me');
    });

  });

});

