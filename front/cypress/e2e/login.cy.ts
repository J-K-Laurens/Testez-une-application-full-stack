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
  })


  // ERREUR CLIENT 400 (Bad Request)
  it('Devrait afficher une erreur pour des identifiants invalides (400)', () => {
    cy.visit('/login');
    mockError('POST', '/api/auth/login', 400, 'Invalid credentials');

    fillLoginForm('invalid@test.com', 'wrongpass');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@error400');
    shouldShowError();
    cy.url().should('include', '/login');
  })


  // ERREUR 401 (Unauthorized - identifiants incorrects)
  it('Devrait afficher une erreur pour un mot de passe incorrect (401)', () => {
    cy.visit('/login');
    mockError('POST', '/api/auth/login', 401, 'Unauthorized');

    fillLoginForm('yoga@studio.com', 'wrongpassword');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@error401');
    shouldShowError();
    cy.url().should('include', '/login');
  })


  // ERREUR SERVEUR 500 (Internal Server Error)
  it('Devrait gérer une erreur serveur 500 (Internal Server Error)', () => {
    cy.visit('/login');
    mockError('POST', '/api/auth/login', 500, 'Internal Server Error');

    fillLoginForm('user@test.com', 'password123');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@error500');
    shouldShowError();
    cy.url().should('include', '/login');
  })


  // ERREUR SERVEUR 502 (Bad Gateway)
  it('Devrait gérer une erreur 502 (Bad Gateway)', () => {
    cy.visit('/login');
    mockError('POST', '/api/auth/login', 502, 'Bad Gateway');

    fillLoginForm('user@test.com', 'password456');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@error502');
    shouldShowError();
    cy.url().should('include', '/login');
  })


  // ERREUR SERVEUR 503 (Service Unavailable)
  it('Devrait gérer une erreur 503 (Service Unavailable)', () => {
    cy.visit('/login');
    mockError('POST', '/api/auth/login', 503, 'Service Unavailable');

    fillLoginForm('user@test.com', 'password789');
    cy.get('input[formControlName=password]').type('{enter}{enter}');

    cy.wait('@error503');
    shouldShowError();
    cy.url().should('include', '/login');
  })


  // VALIDATION DU FORMULAIRE (champs requis)
  it('Devrait désactiver le bouton Submit si le formulaire est invalide', () => {
    cy.visit('/login');

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=password]').type('password123');
    cy.get('button[type=submit]').should('not.be.disabled');
  })


  // VALIDATION DU FORMAT EMAIL
  it('Devrait valider le format de l\'email', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type('invalid-email');
    cy.get('input[formControlName=password]').type('password123');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=email]').clear().type('valid@email.com');
    cy.get('button[type=submit]').should('not.be.disabled');
  })

});
