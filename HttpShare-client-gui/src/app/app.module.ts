import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FileRestService} from './file.rest-service';

import {AppComponent} from './app.component';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [FileRestService],
  bootstrap: [AppComponent]
})
export class AppModule {


}
