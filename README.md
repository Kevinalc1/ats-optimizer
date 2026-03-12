<h1 align="center">🚀 ATS Resume Optimizer</h1>

<p align="center">
  <strong>Otimizador de Currículos com Inteligência Artificial para superar filtros ATS</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black" />
  <img src="https://img.shields.io/badge/Vite-7-646CFF?style=for-the-badge&logo=vite&logoColor=white" />
  <img src="https://img.shields.io/badge/Groq_AI-Llama_3.3-FF6B35?style=for-the-badge&logo=meta&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
</p>

---

## 📖 Sobre o Projeto

O **ATS Resume Optimizer** é uma aplicação **Full Stack** orientada a **Inteligência Artificial**, desenvolvida para ajudar candidatos a ultrapassar os filtros automatizados de recrutamento — os chamados **Sistemas ATS** (*Applicant Tracking Systems*).

A ferramenta executa um pipeline completo e automatizado:

1. **Extrai** o texto de um currículo em PDF via `Apache PDFBox`.
2. **Analisa** semanticamente o currículo em conjunto com a descrição da vaga usando o modelo **Llama 3.3 (70B)** via `Groq API`.
3. **Gera automaticamente** um novo PDF limpo, estruturado e 100% legível por robôs de triagem, usando `OpenPDF`.

---

## ✨ Funcionalidades

| Funcionalidade | Descrição |
|---|---|
| 📄 **Extração Inteligente de PDF** | Leitura nativa de arquivos PDF para extração completa do texto original |
| 🤖 **Otimização com IA (LLM)** | Análise semântica cruzando competências do candidato com os requisitos da vaga |
| 📊 **Score de Compatibilidade** | Pontuação automática indicando o percentual de aderência à vaga |
| 🔍 **Análise de Keywords** | Identificação de palavras-chave presentes e ausentes em relação à vaga |
| 📝 **Geração de PDF Otimizado** | Criação de novo documento PDF estruturado, focado em palavras-chave ATS |
| 🎨 **Interface Moderna** | Frontend React com área de *Drag & Drop* para carregamento intuitivo de arquivos |
| 📚 **Documentação Interativa** | API documentada via Swagger UI (OpenAPI 3) |

---

## 🏗️ Arquitetura do Sistema

```
┌──────────────────────────────────────────────────────────────┐
│                        FRONTEND (React)                       │
│         Upload PDF + Descrição da Vaga via Drag & Drop        │
└──────────────────────┬───────────────────────────────────────┘
                       │ POST /api/curriculo/analisar
                       │ (multipart/form-data)
┌──────────────────────▼───────────────────────────────────────┐
│                    BACKEND (Spring Boot)                       │
│                                                              │
│  ┌───────────────┐   ┌───────────────┐   ┌────────────────┐ │
│  │ResumeController│──▶│PdfParserService│  │  AiService     │ │
│  │  @RestController│   │  (PDFBox)     │  │  (Groq API)    │ │
│  └───────────────┘   └──────┬────────┘   └───────┬────────┘ │
│                             │ texto extraído      │ JSON     │
│                             └──────────────┬──────┘         │
│                                            ▼                 │
│                              ┌─────────────────────────┐     │
│                              │  PdfGeneratorService     │     │
│                              │  (OpenPDF)               │     │
│                              └─────────────────────────┘     │
└──────────────────────────────────────────────────────────────┘
                       │ PDF otimizado (bytes)
┌──────────────────────▼───────────────────────────────────────┐
│              GROQ API — Llama 3.3 (70B Versatile)             │
│          Inferência ultrarrápida de Linguagem Natural          │
└──────────────────────────────────────────────────────────────┘
```

### Fluxo de Processamento (Pipeline)

```
PDF Original  ─▶  Extração de Texto  ─▶  Prompt Engineering  ─▶  LLM (Groq)
                   (Apache PDFBox)                                     │
                                                                       ▼
PDF Otimizado ◀─  Renderização PDF  ◀─  Parsing do JSON  ◀─  Resposta JSON
                    (OpenPDF)            (Jackson)
```

---

## 🛠️ Stack Técnica

### Backend — Java / Spring Boot

| Tecnologia | Versão | Uso |
|---|---|---|
| **Java** | 21 (LTS) | Linguagem principal — Records, Text Blocks, Virtual Threads |
| **Spring Boot** | 3.5.x | Framework principal — IoC, Auto-configuration |
| **Spring Web** | — | API RESTful, `@RestController`, `MultipartFile` |
| **Apache PDFBox** | 3.0.1 | Extração e leitura de texto de arquivos PDF |
| **OpenPDF (LibrePDF)** | 2.0.3 | Geração programática de PDF estilizado |
| **Springdoc OpenAPI** | 2.3.0 | Documentação Swagger UI (OpenAPI 3) |
| **Jackson** | — | Serialização/Desserialização de JSON (`ObjectMapper`, `JsonNode`) |
| **Java HttpClient** | 21 (nativo) | Chamadas HTTP para a Groq API (sem dependência extra) |
| **Maven** | 3.9.x | Gerenciamento de dependências e build |
| **Docker** | — | Containerização com multi-stage build |

### Frontend — React / Vite

| Tecnologia | Versão | Uso |
|---|---|---|
| **React** | 19.x | UI reativa com Hooks e estado local |
| **Vite** | 7.x | Build tool ultrarrápida (HMR instantâneo) |
| **Lucide React** | 0.577.x | Biblioteca moderna de ícones SVG |
| **CSS Customizado** | — | Design Premium com Drag & Drop e animações |
| **ESLint** | 9.x | Linting e qualidade de código |

### Inteligência Artificial

| Tecnologia | Detalhes |
|---|---|
| **Groq API** | Inferência de LLMs em hardware especializado (LPU™) |
| **Modelo** | `llama-3.3-70b-versatile` — Meta Llama 3.3 (70 Bilhões de parâmetros) |
| **Técnica** | Prompt Engineering com output forçado em JSON (`response_format: json_object`) |

---

## 🧱 Estrutura do Projeto

```
ats-optimizer/
├── backend/                        # API Spring Boot
│   ├── src/
│   │   └── main/
│   │       ├── java/com/curriculo/ats_optimizer/
│   │       │   ├── AtsOptimizerApplication.java    # Entry point
│   │       │   ├── controller/
│   │       │   │   └── ResumeController.java        # REST endpoint + Swagger docs
│   │       │   └── service/
│   │       │       ├── AiService.java               # Integração Groq API + Prompt Engineering
│   │       │       ├── PdfParserService.java         # Extração de texto com PDFBox
│   │       │       └── PdfGeneratorService.java      # Geração de PDF com OpenPDF
│   │       └── resources/
│   │           └── application.properties           # Configuração (chave da API)
│   ├── Dockerfile                  # Multi-stage build (Maven → JRE)
│   └── pom.xml                     # Dependências Maven
│
└── frontend/
    └── ats-frontend/               # SPA React + Vite
        ├── src/
        │   ├── App.jsx             # Componente principal com lógica de upload
        │   ├── App.css             # Estilos customizados (Drag & Drop, animações)
        │   └── main.jsx            # Entry point React
        ├── index.html
        ├── vite.config.js
        └── package.json
```

---

## 🚀 Como Executar Localmente

### Pré-requisitos

- [Java JDK 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Node.js 18+ & NPM](https://nodejs.org/)
- Chave de API gratuita da [Groq Cloud](https://console.groq.com/)

---

### 1. Clone o Repositório

```bash
git clone https://github.com/Kevinalc1/ats-optimizer.git
cd ats-optimizer
```

---

### 2. Configurar e Executar o Backend

```bash
cd backend
```

Abra o arquivo `src/main/resources/application.properties` e adicione sua chave:

```properties
groq.api.key=SUA_CHAVE_AQUI
```

> ⚠️ **Importante:** Nunca faça commit da sua chave real para o GitHub. Adicione `application.properties` ao `.gitignore` ou use variáveis de ambiente.

Execute com Maven:

```bash
mvn spring-boot:run
```

A API estará disponível em: **http://localhost:8080**  
Documentação Swagger UI: **http://localhost:8080/swagger-ui/index.html**

---

### 3. Configurar e Executar o Frontend

Em um novo terminal:

```bash
cd frontend/ats-frontend
npm install
npm run dev
```

A aplicação estará disponível em: **http://localhost:5173**

---

### 4. Executar com Docker (Backend)

```bash
cd backend
docker build -t ats-optimizer-backend .
docker run -p 8080:8080 -e GROQ_API_KEY=SUA_CHAVE_AQUI ats-optimizer-backend
```

---

## 📡 Endpoint da API

### `POST /api/curriculo/analisar`

Recebe o currículo em PDF e a descrição da vaga, retorna um PDF otimizado.

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `arquivo` | `MultipartFile` | Arquivo PDF do currículo original |
| `vaga` | `String` | Texto com a descrição da vaga-alvo |

**Resposta:** `application/pdf` — arquivo `Curriculo_Otimizado.pdf`

> A documentação completa e interativa está disponível no **Swagger UI** em `/swagger-ui/index.html`.

---

## 💡 Competências Técnicas Demonstradas

Este projeto demonstra na prática o domínio das seguintes competências:

- ✅ **Arquitetura RESTful** — Design e implementação de APIs com Spring Web
- ✅ **Integração com APIs Externas** — Consumo da Groq API via `java.net.http.HttpClient` nativo
- ✅ **Prompt Engineering** — Construção de prompts estruturados com output em JSON validado
- ✅ **Processamento de Arquivos** — Leitura (PDFBox) e geração (OpenPDF) de documentos PDF
- ✅ **Serialização JSON** — Parsing e mapeamento com Jackson (`ObjectMapper`, `JsonNode`)
- ✅ **Documentação de API** — OpenAPI 3 / Swagger com anotações declarativas
- ✅ **Frontend Reativo** — Componentes React com Hooks, estado assíncrono e UX de Drag & Drop
- ✅ **Containerização** — Docker com multi-stage build (maven → jre) para imagem otimizada
- ✅ **Princípios SOLID** — Separação de responsabilidades (Controller → Service layer)
- ✅ **Gestão de Configurações** — Uso de `@Value` e `application.properties` para secrets

---

## 👨‍💻 Autor

**Kevin Alcantara**  
Desenvolvedor Backend Java | Analista e Desenvolvedor de Sistemas

Estudante de **Análise e Desenvolvimento de Sistemas (2025–2027)** com foco em Backend. Entusiasta por desenvolvimento de APIs RESTful, integração de serviços de IA, código limpo e resolução de problemas complexos. Sempre à procura de construir soluções sólidas, eficientes e bem documentadas.

<p>
  <a href="https://www.linkedin.com/in/kevinalcantara-dev/">
    <img src="https://img.shields.io/badge/LinkedIn-kevinalcantara--dev-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" />
  </a>
  <a href="https://github.com/Kevinalc1">
    <img src="https://img.shields.io/badge/GitHub-Kevinalc1-181717?style=for-the-badge&logo=github&logoColor=white" />
  </a>
  <a href="mailto:kevinalc@outlook.com">
    <img src="https://img.shields.io/badge/Email-kevinalc%40outlook.com-D14836?style=for-the-badge&logo=microsoft-outlook&logoColor=white" />
  </a>
</p>

---

<p align="center">
  Desenvolvido com ☕ Java, 🤖 IA e muito ❤️ por Kevin Alcantara
</p>
