describe('Register - Inscription utilisateur', () => {
  
  // INSCRIPTION RÉUSSIE
  it('Devrait permettre une inscription réussie et rediriger vers /login', () => {
    cy.visit('/register');
    
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: null
    }).as('registerRequest');

    fillRegisterForm('John', 'Doe', 'john.doe@example.com', 'SecurePassword123!');
    submitForm();

    cy.wait('@registerRequest');
    cy.url().should('include', '/login');
    shouldNotShowError();
  })

  
  // GESTION D'ERREUR (email déjà existant)
  it('Devrait afficher une erreur si l\'inscription échoue', () => {
    cy.visit('/register');
    mockError('POST', '/api/auth/register', 400, 'Email already exists');

    fillRegisterForm('Jane', 'Smith', 'existing@example.com', 'Password123!');
    submitForm();

    shouldShowError();
    cy.url().should('include', '/register');
  })

  
  // VALIDATION DU FORMULAIRE (champs requis)
  it('Devrait désactiver le bouton Submit si les champs requis sont vides', () => {
    cy.visit('/register');

    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=firstName]').type('Alice');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=lastName]').type('Wonderland');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=email]').type('alice@example.com');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=password]').type('password123');
    cy.get('button[type=submit]').should('not.be.disabled');
  })

  
  // VALIDATION EMAIL (format invalide)
  it('Devrait désactiver le bouton Submit si le format email est invalide', () => {
    cy.visit('/register');

    fillRegisterForm('John', 'Doe', 'invalid-email', 'password123');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=email]').clear().type('john@example.com');
    cy.get('button[type=submit]').should('not.be.disabled');
  })


  // ERREUR SERVEUR 500 (Internal Server Error)
  it('Devrait gérer une erreur serveur 500 (Internal Server Error)', () => {
    cy.visit('/register');
    mockError('POST', '/api/auth/register', 500, 'Internal Server Error');

    fillRegisterForm('John', 'Doe', 'john.doe@test.com', 'ValidPassword123!');
    submitForm();

    cy.wait('@error500');
    shouldShowError();
    cy.url().should('include', '/register');
  })

});
