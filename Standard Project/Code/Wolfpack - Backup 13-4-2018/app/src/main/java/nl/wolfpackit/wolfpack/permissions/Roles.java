package nl.wolfpackit.wolfpack.permissions;

/**
 * Created by Wolfpack on 4/3/2018.
 */

public enum Roles {
    User(0),
    Admin(1, Permissions.DeclarationVerification);

    int ID;
    Permissions[] perms;
    Roles(int ID, Permissions... perms){
        this.ID = ID;
        this.perms = perms;
    }
    public boolean hasPerm(Permissions perm){
        for(int i=0; i<perms.length; i++){
            if(perms[i]==perm) return true;
        }
        return false;
    }
    public static Roles getByID(int ID){
        for(Roles role: Roles.values()){
            if(role.ID==ID)
                return role;
        }
        return Roles.User;
    }
}
