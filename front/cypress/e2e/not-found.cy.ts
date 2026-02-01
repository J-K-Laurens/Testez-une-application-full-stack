import { loginAsAdmin, loginAsUser, mock404 } from '../support/commands';

  // =========================== PAGE 404 ===========================
  // ==================== Coté Client ====================
  describe('Page 404 - Not Found', () => {
    it('Devrait afficher la page 404 pour une route inexistante', () => {
      cy.visit('/route-inexistante', { failOnStatusCode: false });
      cy.contains('Page not found').should('be.visible');
    });

    it('Devrait afficher la page 404 pour une autre route invalide', () => {
      cy.visit('/xyz/abc/123', { failOnStatusCode: false });
      cy.contains('Page not found').should('be.visible');
    });
  });

  // ==================== Coté Serveur (ressource inexistante) ====================
  describe('Erreurs 404 API - Ressources inexistantes', () => {

    it('Devrait gérer une erreur 404 pour une session inexistante', () => {
      // Mock avec une session dans la liste
      const mockSession = {
        id: 1,
        name: 'Yoga Session',
        description: 'Description test',
        date: '2024-02-15',
        teacher_id: 1,
        users: [],
        createdAt: '2024-01-01',
        updatedAt: '2024-01-10'
      };

      // Setup mock 404 avant le login
      cy.visit('/login');
      cy.intercept('GET', '/api/session', [mockSession]).as('getSessions');
      mock404('/api/session/1', 'Session not found');

      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: { id: 2, username: 'user@studio.com', firstName: 'Regular', lastName: 'User', admin: false }
      }).as('login');
      
      cy.get('input[formControlName=email]').type('user@studio.com');
      cy.get('input[formControlName=password]').type('password123{enter}{enter}');
      cy.wait('@login');
      cy.wait('@getSessions');

      // Cliquer sur la session → 404
      cy.contains('mat-card', 'Yoga Session').should('be.visible');
      cy.contains('mat-card', 'Yoga Session').find('button').contains('Detail').click();
      cy.wait('@notFound404');

      cy.url().should('match', /\/sessions/);
    });

    it('Devrait gérer une erreur 404 pour un utilisateur inexistant', () => {
      loginAsUser();
      mock404('/api/user/2', 'User not found');

      cy.contains('span', 'Account').click();
      cy.wait('@notFound404');

      cy.url().should('include', '/me');
    });

    it('Devrait gérer une erreur 404 sur la liste des teachers', () => {
      loginAsAdmin();
      mock404('/api/teacher', 'Teachers not found');

      cy.contains('Create').click();
      cy.wait('@notFound404');

      cy.url().should('include', '/sessions/create');
    });

  });

