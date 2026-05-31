package cli;

public class CommandInfo {
    private String description;
    private Command action;
    private String requiredRole;
    private int minArgs;
    private String usage;

    public CommandInfo(String description, Command action, String requiredRole, int minArgs, String usage) {
        this.description = description;
        this.action = action;
        this.requiredRole = requiredRole;
        this.minArgs = minArgs;
        this.usage = usage;
    }

    public String getDescription() {
        return description;
    }

    public Command getAction() {
        return action;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public String getUsage() {
        return usage;
    }
}