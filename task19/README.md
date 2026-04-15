# Task 19: Git Repository Operations

## Objective
Create a Git repository for a sample project and demonstrate basic Git operations such as
initializing a repository, staging files, committing changes, and maintaining version history.
Push the repository to GitHub and manage it using remote repositories.

---

## Part 1: Initializing a Git Repository

### Step 1: Create a sample project directory
```bash
mkdir sample-project
cd sample-project
```

### Step 2: Initialize Git
```bash
git init
```
**Output:**
```
Initialized empty Git repository in /path/to/sample-project/.git/
```

### Step 3: Verify the `.git` directory
```bash
ls -la .git/
```
This creates the hidden `.git` directory containing all version control metadata.

---

## Part 2: Staging Files

### Step 4: Create sample files
```bash
# Create a README file
echo "# Sample Project" > README.md
echo "A demo project for learning Git operations." >> README.md

# Create a simple Java file
mkdir -p src/main/java
cat > src/main/java/Main.java << 'EOF'
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Git!");
    }
}
EOF

# Create a .gitignore
cat > .gitignore << 'EOF'
*.class
*.jar
target/
.idea/
*.iml
EOF
```

### Step 5: Check status (untracked files)
```bash
git status
```
**Output:**
```
On branch main

No commits yet

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        .gitignore
        README.md
        src/

nothing added to commit but untracked files present (use "git add" to track)
```

### Step 6: Stage individual files
```bash
# Stage a single file
git add README.md

# Check status — README is staged, others are untracked
git status
```

### Step 7: Stage all files at once
```bash
git add .
git status
```
**Output:**
```
On branch main

No commits yet

Changes to be committed:
  (use "git rm --cached <file>..." to unstage)
        new file:   .gitignore
        new file:   README.md
        new file:   src/main/java/Main.java
```

---

## Part 3: Committing Changes

### Step 8: Make the first commit
```bash
git commit -m "Initial commit: Add README, Main.java, and .gitignore"
```
**Output:**
```
[main (root-commit) abc1234] Initial commit: Add README, Main.java, and .gitignore
 3 files changed, 15 insertions(+)
 create mode 100644 .gitignore
 create mode 100644 README.md
 create mode 100644 src/main/java/Main.java
```

### Step 9: Make changes and commit again
```bash
# Modify Main.java
cat > src/main/java/Main.java << 'EOF'
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Git!");
        System.out.println("Version 2 - Learning commits");
    }
}
EOF

# Stage and commit
git add src/main/java/Main.java
git commit -m "Update Main.java: Add version 2 message"
```

### Step 10: Add a new file and commit
```bash
cat > src/main/java/Calculator.java << 'EOF'
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

git add .
git commit -m "Add Calculator.java with basic arithmetic operations"
```

---

## Part 4: Viewing Version History

### Step 11: View commit log
```bash
git log
```
**Output:**
```
commit def5678 (HEAD -> main)
Author: Davanesh <davanesh@example.com>
Date:   Tue Apr 15 2026

    Add Calculator.java with basic arithmetic operations

commit bcd4567
Author: Davanesh <davanesh@example.com>
Date:   Tue Apr 15 2026

    Update Main.java: Add version 2 message

commit abc1234
Author: Davanesh <davanesh@example.com>
Date:   Tue Apr 15 2026

    Initial commit: Add README, Main.java, and .gitignore
```

### Step 12: View compact log
```bash
git log --oneline
```
**Output:**
```
def5678 (HEAD -> main) Add Calculator.java with basic arithmetic operations
bcd4567 Update Main.java: Add version 2 message
abc1234 Initial commit: Add README, Main.java, and .gitignore
```

### Step 13: View log with graph
```bash
git log --oneline --graph --all
```

### Step 14: View diff between commits
```bash
# See what changed in the last commit
git diff HEAD~1 HEAD

# See what changed in a specific file
git log -p src/main/java/Main.java
```

---

## Part 5: Pushing to GitHub (Remote Repository)

### Step 15: Create a repository on GitHub
1. Go to [https://github.com/new](https://github.com/new)
2. Repository name: `sample-project`
3. Keep it **Public** or **Private**
4. Do **NOT** initialize with README (we already have one)
5. Click **Create repository**

### Step 16: Add the remote origin
```bash
git remote add origin https://github.com/<your-username>/sample-project.git
```

### Step 17: Verify the remote
```bash
git remote -v
```
**Output:**
```
origin  https://github.com/<your-username>/sample-project.git (fetch)
origin  https://github.com/<your-username>/sample-project.git (push)
```

### Step 18: Push to GitHub
```bash
# Push the main branch and set upstream tracking
git push -u origin main
```

### Step 19: Verify on GitHub
- Visit `https://github.com/<your-username>/sample-project`
- You should see all 3 commits and all files

---

## Part 6: Managing Remote Repositories

### Step 20: Clone the repository (simulating another developer)
```bash
cd ..
git clone https://github.com/<your-username>/sample-project.git sample-project-clone
cd sample-project-clone
```

### Step 21: Pull latest changes
```bash
# After someone pushes changes to the remote
git pull origin main
```

### Step 22: Fetch without merging
```bash
git fetch origin
git log origin/main --oneline
```

### Step 23: View remote branches
```bash
git branch -r
```

---

## Summary of Git Commands Used

| Command | Purpose |
|---------|---------|
| `git init` | Initialize a new Git repository |
| `git add <file>` | Stage a specific file |
| `git add .` | Stage all changes |
| `git status` | Check working directory status |
| `git commit -m "msg"` | Commit staged changes |
| `git log` | View commit history |
| `git log --oneline` | View compact commit history |
| `git diff` | View differences between versions |
| `git remote add origin <url>` | Link local repo to remote |
| `git push -u origin main` | Push to remote and set tracking |
| `git pull origin main` | Pull latest changes from remote |
| `git clone <url>` | Clone a remote repository |
| `git fetch` | Download remote changes without merging |
| `git branch -r` | List remote branches |
