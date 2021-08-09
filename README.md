# ek
Personal finance using data from discontinued swedish sw

Ett försök att ersätta Hogia Hemekonomi (Hogia Personal Finance Software) eftersom Hogia har lagt ner det projektet.
För att slippa kasta bort de data jag har så kan jag inte bara byta till något annat program utan skriver ett eget
som kan läsa data från Hemekonomi.

Detta programmet fungerar och klarar att läsa in en existerande databas i både XP (Pro) och Windows 7 (Ultimate).
Fungerar även Windows 10 pro 64-bits med Oracle JRE 7 32-bits (men inte senare eller 64-bits).

Programmet är skrivet i Java och bör därmed fungera på alla plattformar som har en lämplig Java-motor.
Men importen från Hemekonomi fungerar enbart under Windows, troligen krävs det också att filen/databasen kommer från en
Hogia Hemekonomi som körts på Windows (dvs tror inte det fungerar med en som kommer från Mac).
Möjligen kan Access eller MDAC25 behövas men det verkar inte så.

Programmet sparar i ett eget XML-format som troligen är lätt att tolka om du vill föra över data till något annat. 

OBS: Kommer troligen inte att uppdateras mer.
