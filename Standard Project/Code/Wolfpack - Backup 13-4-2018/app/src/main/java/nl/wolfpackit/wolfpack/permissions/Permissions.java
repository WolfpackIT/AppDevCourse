package nl.wolfpackit.wolfpack.permissions;

public enum Permissions {
    DeclarationVerification(0);

    int ID;
    Permissions(int ID){
        this.ID = ID;
    }
}
