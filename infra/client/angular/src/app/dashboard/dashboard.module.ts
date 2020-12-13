import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {DashboardComponent} from "./dashboard.component";
import {MatModule} from "../mat.module";
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [DashboardComponent],
  imports: [CommonModule, MatModule, RouterModule],
  exports: [DashboardComponent]
})
export class DashboardModule {
}
