import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = 'http://localhost:8081/api/v1';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Request-ID': this.generateRequestId()
    });
  }

  private generateRequestId(): string {
    return 'fe-' + Math.random().toString(36).substring(2) + Date.now().toString(36);
  }

  triggerSuccessEndpoint(): Observable<any> {
    return this.http.get(`${this.baseUrl}/proxy/success`, { headers: this.getHeaders() });
  }

  triggerValidationError(): Observable<any> {
    return this.http.get(`${this.baseUrl}/proxy/validation-error`, { headers: this.getHeaders() });
  }

  triggerNotFoundError(): Observable<any> {
    return this.http.get(`${this.baseUrl}/proxy/not-found`, { headers: this.getHeaders() });
  }

  triggerBusinessError(): Observable<any> {
    return this.http.get(`${this.baseUrl}/proxy/business-error`, { headers: this.getHeaders() });
  }

  triggerExternalServiceError(): Observable<any> {
    return this.http.get(`${this.baseUrl}/proxy/external-service-error`, { headers: this.getHeaders() });
  }

  triggerInternalError(): Observable<any> {
    return this.http.get(`${this.baseUrl}/proxy/internal-error`, { headers: this.getHeaders() });
  }

  triggerLocalValidationError(): Observable<any> {
    return this.http.get(`${this.baseUrl}/local-validation-error`, { headers: this.getHeaders() });
  }

  triggerLocalBusinessError(): Observable<any> {
    return this.http.get(`${this.baseUrl}/local-business-error`, { headers: this.getHeaders() });
  }

  validateUser(email: string, name: string): Observable<any> {
    const body = { email, name };
    return this.http.post(`${this.baseUrl}/proxy/validate-user`, body, { headers: this.getHeaders() });
  }

  processOrder(orderData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/process-order`, orderData, { headers: this.getHeaders() });
  }
}