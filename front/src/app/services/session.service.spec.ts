import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with isLogged as false', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should log in a user', () => {
    const mockUser: SessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    service.logIn(mockUser);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockUser);
  });

  it('should log out a user', () => {
    const mockUser: SessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    service.logIn(mockUser);
    service.logOut(); // Go out of the session for the next test

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit isLogged status through $isLogged observable', (done) => {
    const mockUser: SessionInformation = {
      token: 'test-token',
      type: 'Bearer',
      id: 1,
      username: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    let emissionCount = 0;
    
    const expectedValues = [false, true, false];

    // Subscribe to the observable to listen for emissions
    service.$isLogged().subscribe(isLogged => {
      // Verify each emission matches the expected value
      expect(isLogged).toBe(expectedValues[emissionCount]);
      emissionCount++;
      
      // After receiving all 3 emissions, signal Jest that the async test is complete
      if (emissionCount === 3) {
        done();
      }
    });

    // Trigger emissions by changing login state
    service.logIn(mockUser);  // Emission #2: true
    service.logOut();          // Emission #3: false
  });
});
