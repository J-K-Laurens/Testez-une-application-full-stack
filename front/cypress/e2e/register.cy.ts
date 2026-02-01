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
  });

  // ==================== ERREURS 4XX (Client) ====================
  it('Devrait afficher une erreur si l\'email existe déjà (400)', () => {
    cy.visit('/register');
    mockError('POST', '/api/auth/register', 400, 'Email already exists');

    fillRegisterForm('Jane', 'Smith', 'existing@example.com', 'Password123!');
    submitForm();

    cy.wait('@error400');
    shouldShowError();
    cy.url().should('include', '/register');
  });


  // ==================== VALIDATION DU FORMULAIRE ====================
  it('Devrait valider le formulaire (champs requis et format email)', () => {
    cy.visit('/register');

    // Bouton désactivé si formulaire vide
    cy.get('button[type=submit]').should('be.disabled');

    // Bouton désactivé si champs incomplets
    cy.get('input[formControlName=firstName]').type('Alice');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=lastName]').type('Wonderland');
    cy.get('button[type=submit]').should('be.disabled');

    cy.get('input[formControlName=email]').type('alice@example.com');
    cy.get('button[type=submit]').should('be.disabled');

    // Bouton activé si tous les champs valides
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
