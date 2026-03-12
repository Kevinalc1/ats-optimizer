🚀 ATS Resume Optimizer

**ATS Resume Optimizer** é uma aplicação Full Stack orientada a Inteligência Artificial, concebida para ajudar os candidatos a ultrapassar os filtros de recrutamento (Sistemas ATS - *Applicant Tracking Systems*). 

A ferramenta extrai os dados de um currículo em PDF, analisa-os em conjunto com a descrição de uma vaga usando um modelo de linguagem natural (Llama 3 via Groq API), e gera automaticamente um novo PDF otimizado, com um design limpo, reescrito e 100% legível por robôs de triagem.

---

## ✨ Funcionalidades

* **Extração Inteligente:** Leitura nativa de ficheiros PDF para extrair o texto original do currículo.
* **Otimização com IA:** Análise semântica que cruza as competências do candidato com os requisitos da vaga.
* **Geração Automática de PDF:** Criação de um novo documento PDF estruturado, focado em palavras-chave e à prova de sistemas ATS.
* **Interface Moderna:** Frontend em React com área de *Drag & Drop* (arrastar e largar) para carregamento intuitivo de ficheiros.

---

## 🛠️ Tecnologias Utilizadas

### Backend
* **Java 21:** Base robusta e de alta performance.
* **Spring Boot:** Criação da API RESTful e orquestração dos serviços.
* **Springdoc OpenAPI (Swagger):** Documentação interativa da API.
* **Apache PDFBox & OpenPDF:** Manipulação, leitura do PDF original e geração do novo documento estilizado.
* **Jackson:** Processamento e conversão do JSON devolvido pela IA.

### Frontend
* **React + Vite:** Interface rápida e reativa.
* **Lucide React:** Biblioteca moderna de ícones.
* **CSS Customizado:** Design Premium com suporte a *Drag & Drop* e transições suaves.

### Inteligência Artificial
* **Groq API (Llama 3):** Processamento de Linguagem Natural em tempo real (inferência ultrarrápida).

---

## 🚀 Como Executar o Projeto Localmente

### 1. Pré-requisitos
* Java JDK 21+
* Maven
* Node.js & NPM
* Uma chave de API gratuita da [Groq Cloud](https://console.groq.com/)

### 2. Configurar o Backend (Spring Boot)
1. Clone o repositório:
   ```bash
   git clone [https://github.com/Kevinalc1/ats-optimizer.git](https://github.com/Kevinalc1/ats-optimizer.git)
   cd ats-optimizer/backend
No ficheiro src/main/resources/application.properties, adicione a sua chave da Groq:

Properties
groq.api.key=SUA_CHAVE_AQUI
(Aviso: Nunca faça commit da sua chave real para o GitHub!)

Execute o projeto usando a sua IDE (IntelliJ/Eclipse) ou via Maven:

Bash
mvn spring-boot:run
A API e o Swagger estarão disponíveis em: http://localhost:8080/swagger-ui/index.html

3. Configurar o Frontend (React)
Numa nova janela do terminal, vá para a pasta do frontend:

Bash
cd ../frontend
Instale as dependências:

Bash
npm install
Inicie o servidor de desenvolvimento:

Bash
npm run dev
A aplicação web estará disponível no seu navegador em: http://localhost:5173

👨‍💻 Autor
Kevin Alcantara Desenvolvedor Backend Java | Analista e Desenvolvedor de Sistemas

Estudante de Análise e Desenvolvimento de Sistemas (2025-2027) com foco em Backend. Entusiasta por desenvolvimento de APIs RESTful, código limpo e resolução de problemas. Sempre à procura de construir soluções sólidas e eficientes.

🌐 LinkedIn https://www.linkedin.com/in/kevinalcantara-dev/

💻 GitHub https://github.com/Kevinalc1

✉️ kevinalc@outlook.com

Este projeto foi desenvolvido com foco na aplicação prática de Java, Integração de APIs Externas (IA) e Arquitetura Full Stack.
