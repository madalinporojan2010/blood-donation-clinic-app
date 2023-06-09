# Blood Donation Clinic
![Tech Stack](https://github-readme-tech-stack.vercel.app/api/cards?title=Tech%20Stack&lineCount=1&line1=springboot,spring%20boot,b71c1c;apachetomcat,Tomcat,b71c1c;java,java,b71c1c;)

## Introducere
 Acest proiect reprezinta o aplicatie pentru gestionarea unei clinici de donatii de sange, la care se pot face programari individuale, la asistenti/medici alesi dupa propria dorinta. Dupa o programare la clinica, pacientul se prezinta la ora si data confirmata de acesta pe mail, cat si pe site-ul web corespunzator. Aplicatia dispune de urmatoarele functionalitati:

> - Un utilizator se poate programa la o data si ora dorita, din programul dispus pe site-ul web.
> - Dupa programare, pacientul se prezinta la clinica si ii este testat sangele, pentru a determina grupa de sange a acestuia.
> - In functie de cat sange va dona pacientul, se modifica cantitatea de sange corespunzatoare grupei de sange in baza de date.
> - Dupa ce acesta va dona sange, pacientul poate opta pentru a dona si pentru o cauza nobila (gestionata de un cadru medical).
> - Spitalele vor putea colecta sangele.
> - Utilizatorul isi poate creea un program recurent prin care va dona sange constant.

## Diagrama bazei de date

![Database diagram](/docs/db_diagram.png)

Baza de date este compusa din 6 tabele:
> - medical_staff
> > - Tabela destinata cadrelor medicale. Id-ul este reprezentat de CNP-ul cadrului medical. Mai contine si specializarea asistentei/ului cat si numarul de telefon.
> - blood_type
> > - Tabela ce contine tipul si sange alaturi de descrierea sa.
> - donation
> > - Tabela de donatii nobile, gestionate de un cadrul medical.
> - schedule
> > - Tabela utilizata pentru a stoca programarile efectuate de catre utilizator. Contine timpul de sosire, id catre tipul de sange al pacientului ce urmeaza sa doneze, asistenta/ul asignat acestuia cat si id-ul pacientului.
> - patient
> > - Contine date de contact pentru o persoana apropiata in cazul in care pacientul pateste ceva. Id-ul unui pacient este dat de CNP-ul sau. Mai contine si id-ul catre tipul sau de sange, cat si nume, telefon, ani, etc.
> - blood_bank
> > - Contine cantitatea de sange si un id catre tipul de sange, din tabela de sange. Am ales sa folosesc o noua table de blood_type deoarece tabela de pacienti foloseste la randul ei un blood type.

## Detalii implementare endpoints
Aplicatia este impartita structural in 3 nivele: controller, service si repository, pentru a scadea cuplarea claselor si a creste coeziunea acestora.
Pentru realizarea endpoint-urilor am utilizat framework-ul pentru backend, Springboot, gestionarea request-urilor/response-urilor fiind mult mai usoara.
Am realizat 4 exemple de endpoint-uri: POST, PUT, GET si DELETE. Acestea au fost realizate strict pe tabela blood_bank.

Atat pentru POST cat si pentru PUT, se transmite un request body in format JSON, care contine datele ce vor a fii introduse/modificate in baza de date, iar apoi se transmite prin endpoint un response cu un mesaj de succes/eroare alaturi de un http status OK/Internal server error.

In cazul unui GET, se acceseaza endpoint-ul /api/v1/bloodBank si se transmit toate datele, sub forma de array, din baza de date blood_bank, alaturi de un http code OK/No content/Internal server error.

Pentru DELETE, se transmite la /api/v1/bloodBank/{bloodBankId} id-ul entry-ului din tabela blood_bank care va fii sters. Se transmite inapoi un mesaj de succes alaturi de un http code OK/Internal server error.

> **Endpoints:**

> > **General:**
> > - Use this to check the backend application health status.
> > - `/api/v1/health`

> > **Blood Bank model:**
> > - Use **POST** for updating a blood bank entry.
> > - Use **PUT** for creating a blood bank entry.
> > - Use **GET** for fetching all the blood bank entries.
> > - Use **DELETE** for deleting a blood bank entry by Id.
> > - `/api/v1/bloodBank/` **[POST, PUT, GET]**
> > - `/api/v1/bloodBank/{bloodBankId}` **[DELETE]**

> > **Blood Type model:**
> > - Use **POST** for updating a blood type entry.
> > - Use **PUT** for creating a blood type entry.
> > - Use **GET** for fetching all the blood type entries.
> > - Use **DELETE** for deleting a blood type entry by Id.
> > - `/api/v1/bloodType` **[POST, PUT, GET]**
> > - `/api/v1/bloodType{bloodTypeId}` **[DELETE]**

> > **Donation model:**
> > - Use **POST** for updating a donation entry.
> > - Use **PUT** for creating a donation entry.
> > - Use **GET** for fetching all the donation entries.
> > - Use **DELETE** for deleting a donation entry by Id.
> > - `/api/v1/donation` **[POST, PUT, GET]**
> > - `/api/v1/donation{donationId}` **[DELETE]**

> > **Medical Staff model:**
> > - Use **POST** for updating a medical staff entry.
> > - Use **PUT** for creating a medical staff entry.
> > - Use **GET** for fetching all the medical staff entries.
> > - Use **DELETE** for deleting a medical staff entry by Id.
> > - `/api/v1/medicalStaff` **[POST, PUT, GET]**
> > - `/api/v1/medicalStaff{medicalStaffId}` **[DELETE]**

> > **Patient model:**
> > - Use **POST** for updating a patient entry.
> > - Use **PUT** for creating a patient entry.
> > - Use **GET** for fetching all the patient entries.
> > - Use **DELETE** for deleting a patient entry by Id.
> > - `/api/v1/patient` **[POST, PUT, GET]**
> > - `/api/v1/patient{patientId}` **[DELETE]**

> > **Schedule model:**
> > - Use **POST** for updating a schedule entry.
> > - Use **PUT** for creating a schedule entry.
> > - Use **GET** for fetching all the schedule entries.
> > - Use **DELETE** for deleting a schedule entry by Id.
> > - `/api/v1/schedule` **[POST, PUT, GET]**
> > - `/api/v1/schedule{scheduleId}` **[DELETE]**

## Observer design pattern
Datorita incertitudinii pacientilor, care pot sau nu sa isi cunoasca tipul de sange, programarile lor din schedule table trebuie modificata automat in momentul determinarii tipului de sange de cadrul medical. Solutia pentru aceasta problema este utilizarea unui design pattern Observer, care contine o lista de pacienti (PatientObservable), in care sunt adaugati/scosi pacientii in momentul in care acestia sunt introdusi/scosi si in/din baza de date. 
Daca obiectele de tip pacient, proaspat introduse, care nu contin si tipul de sange al pacientului respectiv, sunt modificate cu noul tip de sange atunci toate programarile corespunzatoare lor sunt modificate cu noul tip de sange, prin intermediul metodei de update din clasa PatientObserver.
