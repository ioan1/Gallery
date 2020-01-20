import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with basic auth credentials if available
        const currentUser = window.btoa( 'ioan:kenzofaitduski');
        request = request.clone({
            setHeaders: {
                Authorization: 'Basic ${currentUser}',
                Bidule: 'truc'
            }
        });
        return next.handle(request);
    }
}
