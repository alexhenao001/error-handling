import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ErrorResponse } from '../models/error-response.model';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('HTTP Error intercepted:', error);
        
        let errorResponse: ErrorResponse;
        
        if (error.error && error.error.error) {
          errorResponse = error.error as ErrorResponse;
        } else {
          errorResponse = {
            error: {
              code: 'UNKNOWN_ERROR',
              message: error.message || 'An unexpected error occurred',
              timestamp: new Date().toISOString(),
              requestId: 'frontend-' + Date.now(),
              path: req.url,
              details: {
                statusCode: error.status,
                statusText: error.statusText
              }
            }
          };
        }
        
        return throwError(() => errorResponse);
      })
    );
  }
}