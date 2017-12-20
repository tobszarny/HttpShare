import {Config} from "./config.interface";
import {Injectable} from "@angular/core";
import {ConfigImpl} from "./config-impl.class";

function _config(): any {
  // noinspection TypeScriptUnresolvedVariable
  return (<any>window).CONFIG.apiUrl;
}

@Injectable()
export class ConfigRef {
  get nativeConfig(): Config {
    return new ConfigImpl(_config());
  };
}
