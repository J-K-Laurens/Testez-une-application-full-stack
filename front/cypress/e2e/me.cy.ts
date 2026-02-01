describe('Me - Page profil utilisateur', () => {

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
      cy.contains('button', 'Delete').should('not.exist');
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

      cy.contains('button', 'Delete').click();
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

      cy.contains('button', 'Delete').click();
      cy.wait('@deleteError');
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

