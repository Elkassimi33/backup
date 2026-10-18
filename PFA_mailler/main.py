import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.application import MIMEApplication
import time
import os
import json
from dotenv import load_dotenv

# ===== CHARGEMENT .env =====
load_dotenv(override=True)

# ===== CONFIGURATION =====
SMTP_SERVER    = "smtp.gmail.com"
SMTP_PORT      = 587
EMAIL_FROM     = os.getenv("EMAIL_FROM")
EMAIL_PASSWORD = os.getenv("EMAIL_PASSWORD")
EMAIL_SUBJECT  = "Candidature Stage PFA 2026"

EMAIL_BODY = """
Bonjour,
J'espère que vous allez bien.

Je me permets de vous contacter dans le cadre de ma recherche d'un stage de fin d'année (PFA).
Je suis actuellement étudiant en première année de Master Réseaux et Systèmes Informatiques (MST RSI) à la Faculté des Sciences et Techniques de Settat (FSTS). Je suis passionné par le développement logiciel, les technologies réseaux et la sécurité informatique.
Durant mon parcours, j'ai eu l'occasion de réaliser plusieurs projets concrets :

▸ Plateforme e-commerce complète : API REST Spring Boot + interface Flutter

▸ Système de détection de personnes en temps réel (Python, OpenCV, YOLO) avec dashboard web et déploiement sur AWS EC2

▸ University Management System : Spring Boot + React.js avec authentification JWT

▸ Application Healthcare mobile développée sous Flutter / Dart
Sur le plan technique, je maîtrise :

▸ Langages : Java, Python, JavaScript, PHP, C, Dart, Bash/Shell

▸ Frameworks : Spring Boot, React.js, Flutter

▸ Bases de données : MySQL, Oracle PL/SQL, bases de données réparties

▸ Cloud & DevOps : AWS (EC2, S3), Git / GitHub

▸ Réseaux & Systèmes : Administration système, sécurité réseau, Cisco Packet Tracer

▸ Outils : IntelliJ IDEA, VS Code, UML, JUnit 5
Convaincu que votre entreprise représente un environnement stimulant pour mettre en pratique mes compétences, je serais ravi de pouvoir contribuer activement à vos projets.
Je reste disponible pour tout entretien ou information complémentaire, et vous adresse en pièce jointe mon CV pour plus de détails.
Dans l'attente de votre retour, je vous adresse mes cordiales salutations.

Youssef El Kassimi
elkassimiyoussef100@gmail.com | 06 13 25 90 21
github.com/youssefelkassimi
"""

PDF_PATH      = "assets/CV.pdf"
JSON_PATH     = "emails.json"
DELAY_SECONDS = 5

# ===== FONCTION D'ENVOI =====
def send_emails():
    if not os.path.exists(PDF_PATH):
        print(f"❌ PDF introuvable : {PDF_PATH}")
        return

    with open(JSON_PATH, "r") as f:
        destinataires = json.load(f)

    total = len(destinataires)
    print(f"📋 {total} emails chargés depuis {JSON_PATH}")
    print("-" * 45)

    try:
        print("🔌 Connexion au serveur Gmail...")
        server = smtplib.SMTP(SMTP_SERVER, SMTP_PORT)
        server.starttls()
        server.login(EMAIL_FROM, EMAIL_PASSWORD)
        print("✅ Connecté !\n")

        for i, dest in enumerate(destinataires, 1):
            msg = MIMEMultipart()
            msg['From']    = EMAIL_FROM
            msg['To']      = dest
            msg['Subject'] = EMAIL_SUBJECT
            msg.attach(MIMEText(EMAIL_BODY, 'plain'))

            with open(PDF_PATH, "rb") as pdf_file:
                pdf_attachment = MIMEApplication(pdf_file.read(), _subtype="pdf")
                pdf_attachment.add_header(
                    'Content-Disposition', 'attachment',
                    filename=os.path.basename(PDF_PATH)
                )
                msg.attach(pdf_attachment)

            server.sendmail(EMAIL_FROM, dest, msg.as_string())
            print(f"✅ [{i}/{total}] Envoyé à {dest}")
            time.sleep(DELAY_SECONDS)

        server.quit()
        print("\n" + "=" * 45)
        print(f"🎉 {total} emails envoyés avec succès !")
        print("📅 Remplace emails.json par la prochaine liste et relance demain.")

    except Exception as e:
        print(f"\n❌ Erreur de connexion : {str(e)}")

# ===== LANCEMENT =====
if __name__ == "__main__":
    send_emails()