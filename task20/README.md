# Task 20: Git Branching Strategies — Merge, Rebase & Conflict Resolution

## Objective
Implement branching strategies in Git by creating feature branches. Perform merging and
rebasing operations, and intentionally create merge conflicts to understand and resolve them.

---

## Part 1: Understanding Branching

### Git Branch Workflow Diagram
```
main:      A --- B --- C ----------- M (merge commit)
                  \                 /
feature:           D --- E --- F ---
```

### Step 1: Setup — Initialize a project with an initial commit
```bash
mkdir branching-demo
cd branching-demo
git init

# Create initial file
cat > app.java << 'EOF'
public class App {
    public static void main(String[] args) {
        System.out.println("Main Application v1.0");
    }

    public String greet(String name) {
        return "Hello, " + name;
    }
}
EOF

git add .
git commit -m "Initial commit: Add App.java v1.0"
```

### Step 2: View current branches
```bash
git branch
```
**Output:**
```
* main
```

---

## Part 2: Creating Feature Branches

### Step 3: Create and switch to a feature branch
```bash
# Method 1: Create and switch in one command
git checkout -b feature/add-calculator

# Method 2 (modern): Use switch
git switch -c feature/add-calculator
```

### Step 4: Work on the feature branch
```bash
cat > Calculator.java << 'EOF'
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public int subtract(int a, int b) { return a - b; }
    public int multiply(int a, int b) { return a * b; }
}
EOF

git add Calculator.java
git commit -m "feat: Add Calculator class with basic operations"

# Add more functionality
cat >> Calculator.java << 'EOF'

// Division method added
// public double divide(int a, int b) { return (double) a / b; }
EOF

# Update Calculator with division
cat > Calculator.java << 'EOF'
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public int subtract(int a, int b) { return a - b; }
    public int multiply(int a, int b) { return a * b; }
    public double divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("Cannot divide by zero");
        return (double) a / b;
    }
}
EOF

git add Calculator.java
git commit -m "feat: Add division method with zero-check to Calculator"
```

### Step 5: View all branches
```bash
git branch -a
```
**Output:**
```
* feature/add-calculator
  main
```

---

## Part 3: Merging Branches

### Strategy A: Fast-Forward Merge
When the main branch has no new commits since the feature branch diverged.

```bash
# Switch to main
git checkout main

# Merge feature branch (fast-forward)
git merge feature/add-calculator
```
**Output:**
```
Updating abc1234..def5678
Fast-forward
 Calculator.java | 8 ++++++++
 1 file changed, 8 insertions(+)
 create mode 100644 Calculator.java
```

### Strategy B: Three-Way Merge (with merge commit)
When both branches have new commits.

```bash
# Create another feature branch
git checkout -b feature/add-logger

cat > Logger.java << 'EOF'
public class Logger {
    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }
    public static void error(String message) {
        System.err.println("[ERROR] " + message);
    }
}
EOF

git add Logger.java
git commit -m "feat: Add Logger utility class"

# Go back to main and make a change there too
git checkout main

# Modify app.java on main
cat > app.java << 'EOF'
public class App {
    public static void main(String[] args) {
        System.out.println("Main Application v2.0");
        System.out.println("Now with Calculator support!");
    }

    public String greet(String name) {
        return "Hello, " + name + "! Welcome.";
    }
}
EOF

git add app.java
git commit -m "Update App.java to v2.0 on main"

# Now merge feature/add-logger — this creates a merge commit
git merge feature/add-logger -m "Merge feature/add-logger into main"
```
**Output:**
```
Merge made by the 'ort' strategy.
 Logger.java | 8 ++++++++
 1 file changed, 8 insertions(+)
 create mode 100644 Logger.java
```

### View the merge graph
```bash
git log --oneline --graph --all
```
**Output:**
```
*   ghi7890 (HEAD -> main) Merge feature/add-logger into main
|\
| * fgh6789 (feature/add-logger) feat: Add Logger utility class
* | efg5678 Update App.java to v2.0 on main
|/
* def5678 feat: Add division method with zero-check to Calculator
* cde4567 feat: Add Calculator class with basic operations
* abc1234 Initial commit: Add App.java v1.0
```

---

## Part 4: Rebasing

### What is Rebase?
Rebase moves/replays your feature branch commits on top of the latest main branch,
creating a linear history (no merge commits).

```
BEFORE rebase:
main:      A --- B --- E
                  \
feature:           C --- D

AFTER rebase:
main:      A --- B --- E
                        \
feature:                 C' --- D'
```

### Step 6: Create a feature branch for rebase demo
```bash
git checkout main
git checkout -b feature/add-validator

cat > Validator.java << 'EOF'
public class Validator {
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    public static boolean isPositive(int number) {
        return number > 0;
    }
}
EOF

git add Validator.java
git commit -m "feat: Add Validator utility class"
```

### Step 7: Make changes on main while feature branch exists
```bash
git checkout main

cat > Config.java << 'EOF'
public class Config {
    public static final String APP_NAME = "BranchDemo";
    public static final String VERSION = "2.1";
}
EOF

git add Config.java
git commit -m "Add Config.java with app constants"
```

### Step 8: Rebase feature branch onto main
```bash
git checkout feature/add-validator
git rebase main
```
**Output:**
```
Successfully rebased and updated refs/heads/feature/add-validator.
```

### Step 9: Now do a fast-forward merge (clean linear history)
```bash
git checkout main
git merge feature/add-validator
```

### View the clean linear history
```bash
git log --oneline --graph
```
**Output:**
```
* jkl0123 (HEAD -> main, feature/add-validator) feat: Add Validator utility class
* ijk9012 Add Config.java with app constants
*   ghi7890 Merge feature/add-logger into main
...
```

---

## Part 5: Intentional Merge Conflicts & Resolution

### Step 10: Create a conflict scenario

```bash
# Create a new branch from main
git checkout main
git checkout -b feature/update-greet

# Modify the greet method in app.java on the feature branch
cat > app.java << 'EOF'
public class App {
    public static void main(String[] args) {
        System.out.println("Main Application v2.0");
        System.out.println("Now with Calculator support!");
    }

    // Updated by feature/update-greet branch
    public String greet(String name) {
        return "Hey there, " + name + "! How are you doing?";
    }
}
EOF

git add app.java
git commit -m "feature: Update greet method with casual style"
```

### Step 11: Make a conflicting change on main
```bash
git checkout main

# Modify the SAME greet method differently on main
cat > app.java << 'EOF'
public class App {
    public static void main(String[] args) {
        System.out.println("Main Application v2.0");
        System.out.println("Now with Calculator support!");
    }

    // Updated by main branch
    public String greet(String name) {
        return "Good day, " + name + ". Welcome to our application.";
    }
}
EOF

git add app.java
git commit -m "main: Update greet method with formal style"
```

### Step 12: Attempt to merge — CONFLICT!
```bash
git merge feature/update-greet
```
**Output:**
```
Auto-merging app.java
CONFLICT (content): Merge conflict in app.java
Automatic merge failed; fix conflicts and then commit the result.
```

### Step 13: View the conflict markers
```bash
git status
cat app.java
```
**Conflicted file content:**
```java
public class App {
    public static void main(String[] args) {
        System.out.println("Main Application v2.0");
        System.out.println("Now with Calculator support!");
    }

<<<<<<< HEAD
    // Updated by main branch
    public String greet(String name) {
        return "Good day, " + name + ". Welcome to our application.";
    }
=======
    // Updated by feature/update-greet branch
    public String greet(String name) {
        return "Hey there, " + name + "! How are you doing?";
    }
>>>>>>> feature/update-greet
}
```

### Step 14: Resolve the conflict
Edit `app.java` to combine both changes or pick one:

```java
public class App {
    public static void main(String[] args) {
        System.out.println("Main Application v2.0");
        System.out.println("Now with Calculator support!");
    }

    // Resolved: Combined formal and casual greetings
    public String greet(String name) {
        return "Hello, " + name + "! Welcome to our application.";
    }

    public String casualGreet(String name) {
        return "Hey there, " + name + "! How are you doing?";
    }

    public String formalGreet(String name) {
        return "Good day, " + name + ". Welcome to our application.";
    }
}
```

### Step 15: Complete the merge
```bash
# Stage the resolved file
git add app.java

# Commit the merge resolution
git commit -m "Resolve merge conflict: Combine greet methods from main and feature"
```

### Step 16: Verify the resolution
```bash
git log --oneline --graph -5
```
**Output:**
```
*   mno3456 (HEAD -> main) Resolve merge conflict: Combine greet methods
|\
| * lmn2345 (feature/update-greet) feature: Update greet method with casual style
* | klm1234 main: Update greet method with formal style
|/
* jkl0123 feat: Add Validator utility class
* ijk9012 Add Config.java with app constants
```

---

## Part 6: Rebase Conflict Resolution

### Step 17: Create a rebase conflict
```bash
git checkout main
git checkout -b feature/rebase-conflict

# Modify Config.java on feature branch
cat > Config.java << 'EOF'
public class Config {
    public static final String APP_NAME = "BranchDemo";
    public static final String VERSION = "3.0-beta";    // Feature branch version
    public static final boolean DEBUG = true;
}
EOF

git add Config.java
git commit -m "feature: Update Config version to 3.0-beta"

# Make conflicting change on main
git checkout main

cat > Config.java << 'EOF'
public class Config {
    public static final String APP_NAME = "BranchDemo";
    public static final String VERSION = "2.5-stable";   // Main branch version
    public static final int MAX_RETRIES = 3;
}
EOF

git add Config.java
git commit -m "main: Update Config version to 2.5-stable"
```

### Step 18: Rebase and resolve
```bash
git checkout feature/rebase-conflict
git rebase main
```
**Output:**
```
CONFLICT (content): Merge conflict in Config.java
error: could not apply abc1234... feature: Update Config version to 3.0-beta
```

### Step 19: Resolve the rebase conflict
```bash
# View and fix Config.java (remove conflict markers, keep desired content)
cat > Config.java << 'EOF'
public class Config {
    public static final String APP_NAME = "BranchDemo";
    public static final String VERSION = "3.0-stable";   // Resolved version
    public static final boolean DEBUG = true;
    public static final int MAX_RETRIES = 3;
}
EOF

# Continue the rebase
git add Config.java
git rebase --continue
```

### Step 20: Abort a rebase (if needed)
```bash
# If you want to undo a rebase mid-way:
git rebase --abort
```

---

## Summary: Merge vs Rebase

| Aspect | Merge | Rebase |
|--------|-------|--------|
| **History** | Non-linear (preserves branch history) | Linear (clean, straight-line history) |
| **Merge Commits** | Creates merge commits | No merge commits |
| **Use Case** | Public/shared branches | Local/feature branches before merging |
| **Safety** | Safer — never rewrites history | Rewrites commit history — avoid on shared branches |
| **Command** | `git merge <branch>` | `git rebase <branch>` |

## Key Commands Reference

| Command | Purpose |
|---------|---------|
| `git branch <name>` | Create a new branch |
| `git checkout -b <name>` | Create and switch to a new branch |
| `git switch -c <name>` | Create and switch (modern syntax) |
| `git branch -a` | List all branches (local + remote) |
| `git merge <branch>` | Merge a branch into current branch |
| `git rebase <branch>` | Rebase current branch onto another |
| `git rebase --continue` | Continue rebase after resolving conflict |
| `git rebase --abort` | Abort an in-progress rebase |
| `git branch -d <name>` | Delete a merged branch |
| `git log --graph --oneline` | View branch history visually |
