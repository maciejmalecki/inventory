import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export interface ItemClassHeader {
  name: string,
  description: string
}

@Injectable({
  providedIn: "root"
})
export class ItemClassService {

  constructor(private readonly httpClient: HttpClient) {
  }

  getAllItems(): Observable<Array<ItemClassHeader>> {
    return this.httpClient.get<Array<ItemClassHeader>>("/api/itemClasses");
  }
}
