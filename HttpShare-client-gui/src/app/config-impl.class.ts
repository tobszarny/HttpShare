import {Config} from "./config.interface";

export class ConfigImpl implements Config {
  apiUrl: string;

  constructor(apiUrl: string) {
    this.apiUrl = apiUrl;
  }

}
