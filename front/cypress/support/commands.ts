// ==================== DÉCLARATIONS TYPESCRIPT ====================
declare global {
  // Données
  const mockSessions: typeof mockSessionsData;
  const mockTeachers: typeof mockTeachersData;
  const adminUser: typeof adminUserData;
  const regularUser: typeof regularUserData;

  // Commandes
  function loginAsAdmin(): void;
  function loginAsUser(customSessions?: any[]): void;
  function mock404(url: string, message?: string): void;
  function mockError(method: 'GET' | 'POST' | 'PUT' | 'DELETE', url: string, statusCode: number, message?: string): void;
  function fillLoginForm(email: string, password: string): void;
  function fillRegisterForm(firstName: string, lastName: string, email: string, password: string): void;
  function submitForm(): void;
  function shouldShowError(message?: string): void;
  function shouldNotShowError(): void;
}

// ==================== DONNÉES DE TEST ====================
const mockSessionsData = [
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

const mockTeachersData = [
    { id: 1, firstName: 'Jean', lastName: 'Dupont', createdAt: '2024-01-01', updatedAt: '2024-01-01' },
    { id: 2, firstName: 'Marie', lastName: 'Martin', createdAt: '2024-01-01', updatedAt: '2024-01-01' }
];

const adminUserData = {
    id: 1,
    username: 'admin@studio.com',
    firstName: 'Admin',
    lastName: 'User',
    email: 'admin@studio.com',
    admin: true,
    createdAt: '2024-01-01T00:00:00.000Z',
    updatedAt: '2024-01-01T00:00:00.000Z'
};

const regularUserData = {
    id: 2,
    username: 'user@studio.com',
    firstName: 'Regular',
    lastName: 'User',
    email: 'user@studio.com',
    admin: false,
    createdAt: '2024-01-15T00:00:00.000Z',
    updatedAt: '2024-01-20T00:00:00.000Z'
};

// Exporter les données globalement
(global as any).mockSessions = mockSessionsData;
(global as any).mockTeachers = mockTeachersData;
(global as any).adminUser = adminUserData;
(global as any).regularUser = regularUserData;

// ==================== Commandes : LOGIN & SESSIONS ====================
(global as any).loginAsAdmin = () => {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: { id: 1, username: 'admin@studio.com', firstName: 'Admin', lastName: 'User', admin: true }
    }).as('loginAdmin');
    cy.intercept('GET', '/api/session', mockSessionsData).as('getSessions');
    cy.get('input[formControlName=email]').type('admin@studio.com');
    cy.get('input[formControlName=password]').type('password123{enter}{enter}');
    cy.wait('@loginAdmin');
    cy.wait('@getSessions');
};

(global as any).loginAsUser = (customSessions = mockSessionsData) => {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: { id: 2, username: 'user@studio.com', firstName: 'Regular', lastName: 'User', admin: false }
    }).as('loginUser');
    cy.intercept('GET', '/api/session', customSessions).as('getSessions');
    cy.get('input[formControlName=email]').type('user@studio.com');
    cy.get('input[formControlName=password]').type('password123{enter}{enter}');
    cy.wait('@loginUser');
    cy.wait('@getSessions');
};

// ==================== Commandes : MOCKS ERREURS HTTP ====================
(global as any).mock404 = (url: string, message = 'Resource not found') => {
    cy.intercept('GET', url, {
      statusCode: 404,
      body: { message }
    }).as('notFound404');
};

(global as any).mockError = (method: 'GET' | 'POST' | 'PUT' | 'DELETE', url: string, statusCode: number, message = 'Error occurred') => {
    cy.intercept(method, url, {
      statusCode,
      body: { message }
    }).as(`error${statusCode}`);
};

// ==================== Commandes : FORMULAIRES ====================
(global as any).fillLoginForm = (email: string, password: string) => {
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
};

(global as any).fillRegisterForm = (firstName: string, lastName: string, email: string, password: string) => {
    cy.get('input[formControlName=firstName]').type(firstName);
    cy.get('input[formControlName=lastName]').type(lastName);
    cy.get('input[formControlName=email]').type(email);
    cy.get('input[formControlName=password]').type(password);
};

(global as any).submitForm = () => {
    cy.get('button[type=submit]').click();
};

// ==================== Commandes : VÉRIFICATIONS ====================
(global as any).shouldShowError = (message = 'An error occurred') => {
    cy.get('.error').should('be.visible').should('contain', message);
};

(global as any).shouldNotShowError = () => {
    cy.get('.error').should('not.exist');
  };

// Exporter vide pour transformer ce fichier en module ES
export {};
