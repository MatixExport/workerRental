import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'rental.worker',
  appName: 'WorkerRental',
  webDir: 'dist',
  android:{
    allowMixedContent:true
  }
  
};

export default config;
