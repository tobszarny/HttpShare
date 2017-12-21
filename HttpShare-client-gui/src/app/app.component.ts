import {Component} from '@angular/core';
import {FileRestService} from "./file.rest-service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private fileRestService: FileRestService) {

  }

  files = ['ala', 'ma', 'kota'];

  getFiles() {
    return this.fileRestService.getFiles();
  }


}
