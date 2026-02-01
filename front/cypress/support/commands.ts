 
  // ==================== DONNÉES DE TEST ====================
  export const mockSessions = [
    {
      id: 1,
      name: 'Yoga matinal',
      description: 'Session de yoga pour bien commencer la journée',
      date: '2024-02-15',
      teacher_id: 1,
      users: [2, 3],
      createdAt: '2024-01-01',
      updatedAt: '2024-01-10'
    },
    {
      id: 2,
      name: 'Yoga relaxation',
      description: 'Session de relaxation en fin de journée',
      date: '2024-02-20',
      teacher_id: 2,
      users: [1],
      createdAt: '2024-01-05',
      updatedAt: '2024-01-12'
    }
  ];

  export const mockTeachers = [
    { id: 1, firstName: 'Jean', lastName: 'Dupont', createdAt: '2024-01-01', updatedAt: '2024-01-01' },
    { id: 2, firstName: 'Marie', lastName: 'Martin', createdAt: '2024-01-01', updatedAt: '2024-01-01' }
  ];

  export const adminUser = {
    id: 1,
    username: 'admin@studio.com',
    firstName: 'Admin',
    lastName: 'User',
    email: 'admin@studio.com',
    admin: true,
    createdAt: '2024-01-01T00:00:00.000Z',
    updatedAt: '2024-01-01T00:00:00.000Z'
  };

  export const regularUser = {
    id: 2,
    username: 'user@studio.com',
    firstName: 'Regular',
    lastName: 'User',
    email: 'user@studio.com',
    admin: false,
    createdAt: '2024-01-15T00:00:00.000Z',
    updatedAt: '2024-01-20T00:00:00.000Z'
  };


  
  // ==================== Commandes : LOGIN & SESSIONS ====================
  export const loginAsAdmin = () => {
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

  export const loginAsUser = () => {
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
