package nl.wolfpackit.wolfpack.modules.declarations;

import nl.wolfpackit.wolfpack.R;
import nl.wolfpackit.wolfpack.modules.ModuleFragment;

public class DeclarationModule extends ModuleFragment {
    public static DeclarationModule createInstance() {
        ModuleFragment f = ModuleFragment.createInstance(R.layout.module_declarations, DeclarationModule.class);
        return (DeclarationModule)f;
    }

    protected void setup() {
        super.setup();
    }

    public String getToolbarText(){
        return getString(R.string.nav_declarations);
    }
    public int getToolbar(){
        return R.id.declarationsToolbar;
    }
}
