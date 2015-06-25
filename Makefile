JC=javac
JFLAGS=-Xlint:unchecked
CLASSPATH=lib/AppPAL.jar:.

MANIFEST=Manifest.txt
TARGET_JAR=genstore.jar

%.class: %.java
	$(JC) $(JFLAGS) -cp $(CLASSPATH) $<

# Java dependencies must be checked in to be built
SRC=$(shell git ls-files \*.java)

# WARNING: use GNU make or build each step in all in order ;-)
all: | classes jar

jar: $(TARGET_JAR)

# Compile each Java source file
classes: $(SRC:.java=.class)
	echo $<

$(TARGET_JAR): classes $(MANIFEST)
	jar cfm $(@) $(MANIFEST) genstore lib

clean:
	$(RM) $(shell find -name \*.class) $(PARSER_SRC) $(TARGET_JAR)

