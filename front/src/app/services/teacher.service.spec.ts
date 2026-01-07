import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verify that no unmatched requests are outstanding
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all teachers', () => {
    const mockTeachers: Teacher[] = [
      {
        id: 1,
        lastName: 'DELAHAYE',
        firstName: 'Margot',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        id: 2,
        lastName: 'THIERCELIN',
        firstName: 'Hélène',
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
      expect(teachers.length).toBe(2);
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should retrieve a teacher by id', () => {
    const mockTeacher: Teacher = {
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
      expect(teacher.id).toBe(1);
    });

    const req = httpMock.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});
