describe('Navigation et Authentification', () => {

  // ==================== PAGE D'ACCUEIL ====================
  describe('Page d\'accueil (non connecté)', () => {

    it('Devrait afficher la barre de navigation avec Login et Register', () => {
      cy.visit('/');
      cy.contains('Yoga app').should('be.visible');
      cy.contains('span', 'Login').should('be.visible');
      cy.contains('span', 'Register').should('be.visible');
    });

    it('Ne devrait PAS afficher Sessions, Account, Logout quand non connecté', () => {
      cy.visit('/');
      cy.contains('span', 'Sessions').should('not.exist');
      cy.contains('span', 'Account').should('not.exist');
      cy.contains('span', 'Logout').should('not.exist');
    });

    it('Devrait naviguer vers Login', () => {
      cy.visit('/');
      cy.contains('span', 'Login').click();
      cy.url().should('include', '/login');
    });

    it('Devrait naviguer vers Register', () => {
      cy.visit('/');
      cy.contains('span', 'Register').click();
      cy.url().should('include', '/register');
    });

  });


  // ==================== NAVIGATION CONNECTÉ ====================
  describe('Navigation (connecté)', () => {

    beforeEach(() => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: adminUser }).as('login');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
      cy.get('input[formControlName=email]').type('admin@studio.com');
      cy.get('input[formControlName=password]').type('password123{enter}{enter}');
      cy.wait('@login');
      cy.wait('@getSessions');
    });

    it('Devrait afficher Sessions, Account, Logout quand connecté', () => {
      cy.contains('span', 'Sessions').should('be.visible');
      cy.contains('span', 'Account').should('be.visible');
      cy.contains('span', 'Logout').should('be.visible');
    });

    it('Ne devrait PAS afficher Login et Register quand connecté', () => {
      cy.contains('span', 'Login').should('not.exist');
      cy.contains('span', 'Register').should('not.exist');
    });

    it('Devrait naviguer vers Sessions', () => {
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
      cy.contains('span', 'Sessions').click();
      cy.url().should('include', '/sessions');
    });

    it('Devrait naviguer vers Account', () => {
      cy.intercept('GET', '/api/user/1', { ...adminUser, email: 'admin@studio.com', createdAt: '2024-01-01', updatedAt: '2024-01-01' }).as('getUser');
      cy.contains('span', 'Account').click();
      cy.wait('@getUser');
      cy.url().should('include', '/me');
    });

  });


  // ==================== LOGOUT ====================
  describe('Déconnexion', () => {

    it('Devrait déconnecter l\'utilisateur et rediriger vers l\'accueil', () => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: adminUser }).as('login');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
      cy.get('input[formControlName=email]').type('admin@studio.com');
      cy.get('input[formControlName=password]').type('password123{enter}{enter}');
      cy.wait('@login');
      cy.wait('@getSessions');

      cy.contains('span', 'Logout').click();

      cy.url().should('include', '/');
      cy.contains('span', 'Login').should('be.visible');
      cy.contains('span', 'Register').should('be.visible');
      cy.contains('span', 'Sessions').should('not.exist');
    });

  });


  // ==================== GUARDS (Protection des routes) ====================
  describe('Guards - Protection des routes', () => {

    it('Devrait rediriger vers login si non connecté et accès à /sessions', () => {
      cy.visit('/sessions');
      cy.url().should('include', '/login');
    });

    it('Devrait rediriger vers login si non connecté et accès à /me', () => {
      cy.visit('/me');
      cy.url().should('include', '/login');
    });

    it('Devrait empêcher un utilisateur connecté d\'accéder à /login via navigation', () => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: adminUser }).as('login');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
      cy.get('input[formControlName=email]').type('admin@studio.com');
      cy.get('input[formControlName=password]').type('password123{enter}{enter}');
      cy.wait('@login');
      cy.wait('@getSessions');

      // Vérifier que les liens Login/Register ne sont plus visibles
      cy.contains('span', 'Login').should('not.exist');
      cy.contains('span', 'Register').should('not.exist');
      cy.url().should('include', '/sessions');
    });

    it('Devrait afficher les liens Sessions/Account/Logout après connexion', () => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', { statusCode: 200, body: adminUser }).as('login');
      cy.intercept('GET', '/api/session', mockSessions).as('getSessions');
      cy.get('input[formControlName=email]').type('admin@studio.com');
      cy.get('input[formControlName=password]').type('password123{enter}{enter}');
      cy.wait('@login');
      cy.wait('@getSessions');

      cy.contains('span', 'Sessions').should('be.visible');
      cy.contains('span', 'Account').should('be.visible');
      cy.contains('span', 'Logout').should('be.visible');
    });

  });



});

