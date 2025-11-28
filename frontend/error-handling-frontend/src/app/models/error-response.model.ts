export interface ErrorResponse {
  error: {
    code: string;
    message: string;
    timestamp: string;
    requestId: string;
    path: string;
    details?: { [key: string]: any };
  };
}