import { Injectable } from '@angular/core';
import { GrowthBook } from "@growthbook/growthbook";

//This endpoint has to be changed depending on your endpoint.
const FEATURES_ENDPOINT = "http://localhost:3100/api/features/prod_MDo4Fwsy6L4jOEpDFJIKJwinsjjj2zy9GfzfNA9tKtc";

@Injectable({
  providedIn: 'root'
})
export class ABTestingService {

  State: boolean

  UserValues = {
    "center-title":false
  }


  private growthbook: GrowthBook
  constructor() {
  }

  async init(): Promise<void> {
    // Create a GrowthBook instance
    this.growthbook = new GrowthBook({
      trackingCallback: (experiment, result) => {
        console.log({
          experimentId: experiment.key,
          variationId: result.variationId
        })
      }
    });
    // Load feature definitions from API
    // In production, we recommend putting a CDN in front of the API endpoint
    await fetch(FEATURES_ENDPOINT)
      .then((res) => res.json())
      .then((json) => {
        this.growthbook.setFeatures(json.features);
      })
      .catch(() => {
        console.log("Failed to fetch feature definitions from GrowthBook");
      }).then(()=>{

        this.setattribute();
      });
  }

  public setattribute() {
    this.growthbook.setAttributes({
      "id": "test",
      "deviceId": "foo",
      "company": "foo",
      "loggedIn": true,
      "employee": true,
      "country": "foo",
      "browser": "foo",
      "url": "foo"
    });
    this.UserValues["center-title"]= this.growthbook.isOn('center-title');
    console.log(this.growthbook.isOn('center-title'))
  }

  public getState(myFeature: string) {
    console.log(this.UserValues)
    return this.UserValues[myFeature];

  }
}
