package mate.academy.internetshop.model;

public class Role {
    private static Long idGenerator = 0L;
    private final Long id;
    private RoleName roleName;

    public Role() {
        this.id = idGenerator++;
    }

    public Role(RoleName roleName) {
        this();
        this.roleName = roleName;
    }

    public enum RoleName {
        USER, ADMIN;
    }

    public Long getId() {
        return id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public static Role of(String roleName) {
        return new Role(RoleName.valueOf(roleName));
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
