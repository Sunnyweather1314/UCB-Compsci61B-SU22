# Gitlet Design Document
author: David Yin

## Design Document Guidelines

---
---

## 1. Classes and Data Structures

### a. Main

There is no any instance or static variables.

---

### b. GitLet

There are eight static variables. 
They are initialized when creating a GitLet instance. 

#### i. CWD
A file instance for the working directory.

#### ii. REPOSITORY
A file instance for .gitlet directory.

#### iii. STAGING_AREA
A file instance for StagingArea.txt.

#### iv. BRANCHES
A file instance for Branches directory.

#### v. COMMITS
A file instance for Commits directory.

#### vi. BLOBS
A file instance for Blobs directory.

#### vii. HEAD
A file instance for HEAD.txt;

---
### c. Commit

There are four instance variables. They are initialized at the time
when we create a commit instance.

#### i. _message
A String indicating the message for this commit.

#### ii. _parent
A String indicating the Sha1 code of the parent of this commit. 

#### iii. _date
A String indicating the date of this commit.

#### iv. _blobs
A TreeMap mapping from names of the files in this commit to their corresponding Sha1 codes.

---
### d. StagingArea

There are two instance variables.

#### i. _addedFiles
A TreeMap mapping from names of the added files to their corresponding Sha1 codes.

#### ii. _removedFiles;
A TreeMap mapping from names of the removed files to their corresponding Sha1 codes.

---
### e. Branch

There are two instance variables.

#### i. _name
A String indicating the name of that branch.

#### ii. _commit
A String indicating the Sha1 value of the commmit this branch points to.

---
### f. Blob

There are two instance variables.

#### i. _sha1

A String indicating the Sha1 value of the blob

#### ii. _file

A File instance pointing to the original file

---
### g. Head

There are one instance variable.

#### i. _name

A String indicating the name of the branch our head points to.

---

### h. Save

This is an interface that has a method called saveContents. 
Every class except Main, GitLet, Branch implements this interface and
override the saveContents method according to its own specification.

---
---
## 2. Algorithms

### a. Main

The Main class parses different types of commands and handle exceptions raised during
the execution of each command. The Main method in the class initialize a GitLet instance
and differentiate the input command by its first argument and then call **validateNumArgs**
to check whether the input commands specific the correct number of arguments. In addition,
if there are three types of potential exception could be raised when parsing commands, this
is handled by the **failure** method.


### b. GitLet
This is the main body of how each command is implemented. We discuss each command according to
the given project description along with our design.

#### i. init
This method implements the command **"java gitlet.Main init"**.
We first initialize all the necessary directories for our repository.
This includes four directories **.gitlet, Branches, Commits, Blob**.
In addition, we create the first commit with the message **"initial commit"**, 
null parent, and an empty Treemap. Then we initialize a new **StagingArea** instance
and call the **saveContents** method to save the persistence.
Finally, we create a **Branch** instance with **"Master"** that points to the initial commit.Also,
we create a **Head** instance that points to the **"Master"** branch.
We call the **saveContents** method to save the persistence for all above instances whose classes
implement serializable.

#### ii. add
This method implements the command **java gitlet.Main add \[file name\]**.
After validating the number of arguments from Main class, we then check
whether there is an initialized **.gitlet** repository and then check whether
the given file exists in the working directory. After that, we read the old 
**StagingArea** instance from the persistence file **StagingArea.txt**. We check
whether the contents of the given file are exactly the same as the one
in the persistence. If so, we remove the file from the instance by calling **removeAddedFile**
method on the file name and its Sha1 code. Otherwise, we add the given file into the **StagingArea**
and create a new **blob** with the Sha1 code of the given file and its file content. 
Finally, we call the **saveContents** for the **StagingArea** instance and the **Blob** instance
to maintain its persistence in the system.

#### iii. commit
This method implements the command **java gitlet.Main commit \[message\]**.
We add all the files in the added list of StagingArea to the new commit if
they have different contents, compared with the last commit, or newly added.
We also remove the files in the removed list of StagingArea to ensure we have
deleted any file that the user wants to delete. If there is no any new file that needs
to be added or deleted, we raise the exception **"No changes added to the commit."**
Otherwise, we create a new **commit** instance with the given message, the last commit,
and the updated blobs. Then, we clear the StagingArea by creating a new instance and 
move head to the new commit. Finally, we save all the persistence, including **StagingArea,
Head, Commit, and Master** (if necessary).

#### iv. remove
This method implements the command **java gitlet.Main rm \[file name\]**. We
delete the file in the StagingArea if it presents and moves into the removed list.
In addition, if the file with the same contents presents in the last commit, we remove
it from the working directory by calling the method **restrictedDelete** in the Utility.
If either of the above two circumstances is satisfied, we raise the exception **"No reason to remove the file."**
Finally, we save the persistence of the StagingArea to keep it updated.

#### v. log
This method implements the command **java gitlet.Main log**. We display
the information of each commit starting from the one **Head** points to until the initial commit. One thing to be noticed,
in order to efficiently implement this method and **global-log** method. We override the **toString**
methods of the commit class so that it follows the specification of log in the proj spec.

#### vi. globalLog
This method implements the command **java gitlet.Main global-log**. We display all the information
of each commit we have made so far. The order doesn't matter. The format of each commit information 
again must comply with the specification of global-log in the proj spec.

#### vii. find
This method implements the command **java gitlet.Main find \[commit message\]**. Prints out the ids 
of all commits that have the given commit message, one per line. If there are multiple such commits, 
it prints the ids out on separate lines. If no such commit exists, we just raise the exception "Found
no commit with that message."

#### viii. checkOut
This method implements three different types of commands for checkOut. We use the num of arugments
to differentiate each type from one another. We analyze one by one.
##### java gitlet.Main checkout \[commit id\] -- \[file name\]
We simply read the commit with the given id. If it doesn't exist, we raise the exception " No commit with that id exists."
We then find whether there is a file with given name in that commit. If not, we raise the exception **"File does not exist in that commit."**
We then override the version of that file in the current directory by using the method **writeContents**.

##### java gitlet.Main checkout -- \[file name\].  
This is essentially the same as the above one except that we checkout the last commit.
Therefore, we can use the same method **checkOutone** to implement this.

##### java gitlet.Main checkout \[branch name\]
For this one, we checkout all the file in the given branch and overwrite all of them in the working directory.
In addition, we move the **Head** to the current branch and delete any files that are tracked in the current branch 
but are not present in the checked-out branch. We also clear the StagingArea if there is no exception raised.
If the name of the given branch doesn't exist, we raise the exception **No such branch exists.**
If the commit that the given branch points to is the head commit, we raise the exception **No need to checkout the current branch.**
Also, if there is a file untracked by the current branch and would be overwritten by checkout, we raise
the exception **There is an untracked file in the way; delete it, or add and commit it first.**


### ix. status
This method implements the command **java gitlet.Main status**. It displays all the available
branches and mark the head branch with asterisk. It also displays a ll the added files and removed files
in the StagingArea. In addition, it lists out all the files that modified but not staged for commit and
untracked files.

### x. branch

This method implements the command **java gitlet.Main branch \[branch name\]**. It creates a new 
**branch** instance points to the given commit. If the branch name already exists, raise exception 
**A branch with that name already exists.**

### xi. removeBranch

This method implements the command **java gitlet.Main rm-branch \[branch name\]**. It deletes the
given branch by only deleting the file under the **BRANCHES** directory.

### xii. reset

This method implements the command **java gitlet.Main reset \[commit id\]**. We make sure the given commit
id exists. If not, we raise the exception **No commit with that id exists.** We then create a temporary **Branch**
instance that points to the given commit. We then call the **checkOutAll** method to override the files in the working
directory. After that, we make the current branch pointing to the commit that the temporary branch instance.
We also update the head points to the current branch so that it overrides the content when executing the
**checkOutAll** and finally delete the temporary branch instance.
## 3. Persistence

We simply implement every class that needs to be tracked with the interface **Save**.
So whenever we update any instance of that class, we call the overrode method **SaveContents**.
The **.gitlet** directory contains three subdirectories **Commits, Branches, Blobs** and two files
**StagingArea.txt** and **Head.txt**. We then save the instance of each class of the three into the corresponding directory.


## 4. Design Diagram

Attach a picture of your design diagram illustrating the structure of your
classes and data structures. The design diagram should make it easy to 
visualize the structure and workflow of your program.
![image info](C:\Users\david\OneDrive\Desktop\UCB\SPR22\CS61B\repo\proj3\gitlet-design.png)
