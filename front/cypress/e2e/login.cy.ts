describe('Login - Connexion utilisateur', () => {

  // CONNEXION RÉUSSIE
  it('Devrait permettre une connexion réussie et rediriger vers /sessions', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('loginRequest')

    cy.intercept('GET', '/api/session', []).as('session')

    cy.get('input[formControlName=email]').type('yoga@studio.com')
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}')

    cy.wait('@loginRequest')
    cy.url().should('include', '/sessions')
  })


  // ERREUR CLIENT 400 (Bad Request)
  it('Devrait afficher une erreur pour des identifiants invalides (400)', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 400,
      body: { message: 'Invalid credentials' }
    }).as('badRequest')

    cy.get('input[formControlName=email]').type('invalid@test.com')
    cy.get('input[formControlName=password]').type('wrongpass{enter}{enter}')

    cy.wait('@badRequest')

    cy.get('.error').should('be.visible').should('contain', 'An error occurred')
    cy.url().should('include', '/login')
  })


  // ERREUR 401 (Unauthorized - identifiants incorrects)
  it('Devrait afficher une erreur pour un mot de passe incorrect (401)', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Unauthorized' }
    }).as('unauthorized')

    cy.get('input[formControlName=email]').type('yoga@studio.com')
    cy.get('input[formControlName=password]').type('wrongpassword{enter}{enter}')

    cy.wait('@unauthorized')

    cy.get('.error').should('be.visible')
    cy.url().should('include', '/login')
  })


  // ERREUR SERVEUR 500 (Internal Server Error)
  it('Devrait gérer une erreur serveur 500 (Internal Server Error)', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 500,
      body: { message: 'Internal Server Error' }
    }).as('serverError500')

    cy.get('input[formControlName=email]').type('user@test.com')
    cy.get('input[formControlName=password]').type('password123{enter}{enter}')

    cy.wait('@serverError500')

    cy.get('.error').should('be.visible')
    cy.url().should('include', '/login')
  })


  // ERREUR SERVEUR 502 (Bad Gateway)
  it('Devrait gérer une erreur 502 (Bad Gateway)', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 502,
      body: { message: 'Bad Gateway' }
    }).as('badGateway')

    cy.get('input[formControlName=email]').type('user@test.com')
    cy.get('input[formControlName=password]').type('password456{enter}{enter}')

    cy.wait('@badGateway')

    cy.get('.error').should('be.visible')
    cy.url().should('include', '/login')
  })


  // ERREUR SERVEUR 503 (Service Unavailable)
  it('Devrait gérer une erreur 503 (Service Unavailable)', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 503,
      body: { message: 'Service Unavailable' }
    }).as('serviceUnavailable')

    cy.get('input[formControlName=email]').type('user@test.com')
    cy.get('input[formControlName=password]').type('password789{enter}{enter}')

    cy.wait('@serviceUnavailable')

    cy.get('.error').should('be.visible')
    cy.url().should('include', '/login')
  })


  // VALIDATION DU FORMULAIRE (champs requis)
  it('Devrait désactiver le bouton Submit si le formulaire est invalide', () => {
    cy.visit('/login')

    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=email]').type('test@test.com')
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=password]').type('password123')
    cy.get('button[type=submit]').should('not.be.disabled')
  })


  // VALIDATION DU FORMAT EMAIL
  it('Devrait valider le format de l\'email', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type('invalid-email')
    cy.get('input[formControlName=password]').type('password123')
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=email]').clear().type('valid@email.com')
    cy.get('button[type=submit]').should('not.be.disabled')
  })

});
