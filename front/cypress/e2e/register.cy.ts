describe('Register - Inscription utilisateur', () => {
  
  // INSCRIPTION RÉUSSIE
  it('Devrait permettre une inscription réussie et rediriger vers /login', () => {
    cy.visit('/register')
    
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: null
    }).as('registerRequest')

    cy.get('input[formControlName=firstName]').type('John')
    cy.get('input[formControlName=lastName]').type('Doe')
    cy.get('input[formControlName=email]').type('john.doe@example.com')
    cy.get('input[formControlName=password]').type('SecurePassword123!')

    cy.get('button[type=submit]').click()

    cy.wait('@registerRequest')

    cy.url().should('include', '/login')

    cy.get('.error').should('not.exist')
  })

  
  // GESTION D'ERREUR (email déjà existant)
  it('Devrait afficher une erreur si l\'inscription échoue', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        error: 'Email already exists'
      }
    })

    cy.get('input[formControlName=firstName]').type('Jane')
    cy.get('input[formControlName=lastName]').type('Smith')
    cy.get('input[formControlName=email]').type('existing@example.com')
    cy.get('input[formControlName=password]').type('Password123!')

    cy.get('button[type=submit]').click()

    cy.get('.error')
      .should('be.visible')
      .should('contain', 'An error occurred')

    cy.url().should('include', '/register')
  })

  
  // VALIDATION DU FORMULAIRE (champs requis)
  it('Devrait désactiver le bouton Submit si les champs requis sont vides', () => {
    cy.visit('/register')

    // Bouton désactivé au départ (champs vides)
    cy.get('button[type=submit]').should('be.disabled')

    // Remplir seulement certains champs
    cy.get('input[formControlName=firstName]').type('Alice')
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=lastName]').type('Wonderland')
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=email]').type('alice@example.com')
    cy.get('button[type=submit]').should('be.disabled')

    // Une fois tous les champs remplis, bouton activé
    cy.get('input[formControlName=password]').type('password123')
    cy.get('button[type=submit]').should('not.be.disabled')
  })

  
  // VALIDATION EMAIL (format invalide)
  it('Devrait désactiver le bouton Submit si le format email est invalide', () => {
    cy.visit('/register')

    cy.get('input[formControlName=firstName]').type('John')
    cy.get('input[formControlName=lastName]').type('Doe')
    cy.get('input[formControlName=email]').type('invalid-email')
    cy.get('input[formControlName=password]').type('password123')

    // Email invalide = bouton désactivé
    cy.get('button[type=submit]').should('be.disabled')

    // Corriger l'email = bouton activé
    cy.get('input[formControlName=email]').clear().type('john@example.com')
    cy.get('button[type=submit]').should('not.be.disabled')
  })


  // ERREUR SERVEUR 500 (Internal Server Error)
  it('Devrait gérer une erreur serveur 500 (Internal Server Error)', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500,
      body: { message: 'Internal Server Error' }
    }).as('serverError500')

    cy.get('input[formControlName=firstName]').type('John')
    cy.get('input[formControlName=lastName]').type('Doe')
    cy.get('input[formControlName=email]').type('john.doe@test.com')
    cy.get('input[formControlName=password]').type('ValidPassword123!')
    cy.get('button[type=submit]').click()

    cy.wait('@serverError500')
    cy.get('.error').should('be.visible').should('contain', 'An error occurred')
    cy.url().should('include', '/register')
  })

});
