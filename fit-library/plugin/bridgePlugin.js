import extendModule from '../util/extendModule';
import navigator from '../bridge/navigator';
import ui from '../bridge/ui';
import tool from '../bridge/tool';
import page from '../bridge/page';

var BridgePlugin = {};
BridgePlugin.install = (Vue, options) => {
  extendModule(navigator);
  extendModule(ui);
  extendModule(tool);
  extendModule(page);
};

export default BridgePlugin;
