import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FileRestService} from './file.rest-service';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {ConfigRef} from "./config-ref.class";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [FileRestService, ConfigRef],
  bootstrap: [AppComponent]
})
export class AppModule {


}
