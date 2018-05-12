import extendModule from '../util/extendModule';
import navigator from '../bridge/navigator';
import ui from '../bridge/ui';
import tool from '../bridge/tool';
import router from '../bridge/router';

var BridgePlugin = {};
BridgePlugin.install = (Vue, options) => {
  extendModule(navigator);
  extendModule(ui);
  extendModule(tool);
  extendModule(router);
};

export default BridgePlugin;
