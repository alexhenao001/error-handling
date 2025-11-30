import { Component, signal, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from './services/api.service';
import { ErrorResponse } from './models/error-response.model';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Error Handling Demo');
  loading = false;
  lastResponse: any = null;
  lastError: ErrorResponse | null = null;

  userForm = {
    email: '',
    name: ''
  };

  orderForm = {
    email: '',
    name: '',
    productId: ''
  };

  constructor(private apiService: ApiService, private cdr: ChangeDetectorRef) {}

  clearResults() {
    this.lastResponse = null;
    this.lastError = null;
  }

  private handleResponse(response: any) {
    this.loading = false;
    this.lastResponse = response;
    this.lastError = null;
    this.cdr.detectChanges();
  }

  private handleError(error: ErrorResponse) {
    this.loading = false;
    this.lastResponse = null;
    this.lastError = error;
    this.cdr.detectChanges();
  }

  triggerSuccess() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerSuccessEndpoint().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  triggerValidationError() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerValidationError().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  triggerNotFoundError() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerNotFoundError().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  triggerBusinessError() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerBusinessError().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  triggerExternalServiceError() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerExternalServiceError().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  triggerInternalError() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerInternalError().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  triggerLocalValidationError() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerLocalValidationError().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  triggerLocalBusinessError() {
    this.loading = true;
    this.clearResults();
    this.apiService.triggerLocalBusinessError().subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  validateUser() {
    this.loading = true;
    this.clearResults();
    this.apiService.validateUser(this.userForm.email, this.userForm.name).subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }

  processOrder() {
    this.loading = true;
    this.clearResults();
    this.apiService.processOrder(this.orderForm).subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    });
  }
}
