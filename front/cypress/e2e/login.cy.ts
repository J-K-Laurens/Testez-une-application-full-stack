describe('Login - Connexion utilisateur', () => {

  // CONNEXION RÉUSSIE
  it('Devrait permettre une connexion réussie et rediriger vers /sessions', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('loginRequest');

    cy.intercept('GET', '/api/session', []).as('session');

    fillLoginForm('yoga@studio.com', 'test!1234');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@loginRequest');
    cy.url().should('include', '/sessions');
  });

  // ==================== ERREURS 4XX (Client) ====================
  it('Devrait afficher une erreur pour des identifiants incorrects (401)', () => {
    cy.visit('/login');
    mockError('POST', '/api/auth/login', 401, 'Unauthorized');

    fillLoginForm('yoga@studio.com', 'wrongpassword');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@error401');
    shouldShowError();
    cy.url().should('include', '/login');
  });

  // ==================== ERREURS 5XX (Serveur) ====================
  it('Devrait gérer une erreur serveur 500 (Internal Server Error)', () => {
    cy.visit('/login');
    mockError('POST', '/api/auth/login', 500, 'Internal Server Error');

    fillLoginForm('user@test.com', 'password123');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@error500');
    shouldShowError();
    cy.url().should('include', '/login');
  });

  // ==================== VALIDATION DU FORMULAIRE ====================
  it('Devrait valider le formulaire (champs requis et format email)', () => {
    cy.visit('/login');

    // Bouton désactivé si formulaire vide
    cy.get('button[type=submit]').should('be.disabled');

    // Bouton désactivé si email seul
    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('button[type=submit]').should('be.disabled');

    // Bouton activé si formulaire valide
    cy.get('input[formControlName=password]').type('password123');
    cy.get('button[type=submit]').should('not.be.disabled');

    // Bouton désactivé si email invalide
    cy.get('input[formControlName=email]').clear().type('invalid-email');
    cy.get('button[type=submit]').should('be.disabled');

    // Bouton activé si email valide
    cy.get('input[formControlName=email]').clear().type('valid@email.com');
    cy.get('button[type=submit]').should('not.be.disabled');
  });

});
