import {Component, OnInit} from '@angular/core';
import {ItemClassHeader, ItemClassService} from "../shared/services/item-class.service";
import {Observable} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  itemClasses$: Observable<Array<ItemClassHeader>>;

  constructor(
    private readonly itemClassService: ItemClassService,
    private readonly router: Router) {
  }

  ngOnInit(): void {
    this.itemClasses$ = this.itemClassService.getAllItemClasses();
  }

  goToItemClassDetails(name: string) {
    this.router
      .navigate(["itemClasses", name])
      .catch(reason => console.log(reason));
  }
}
