import { useState, useRef } from 'react'
import {
  UploadCloud, FileText, Sparkles, CheckCircle2,
  AlertCircle, Zap, Shield, TrendingUp, ChevronDown, X
} from 'lucide-react'
import './App.css'

const FEATURES = [
  {
    icon: <Zap size={24} />,
    title: 'Análise Instantânea',
    desc: 'Nossa IA analisa seu currículo em segundos e identifica gaps em relação à vaga.',
  },
  {
    icon: <TrendingUp size={24} />,
    title: 'Score ATS',
    desc: 'Receba uma pontuação de compatibilidade e saiba exatamente o que melhorar.',
  },
  {
    icon: <Shield size={24} />,
    title: 'Palavras-chave Certas',
    desc: 'Inserimos automaticamente os termos que os recrutadores e sistemas ATS procuram.',
  },
]

function App() {
  const [file, setFile] = useState(null)
  const [vaga, setVaga] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState(false)
  const [isDragging, setIsDragging] = useState(false)

  const fileInputRef = useRef(null)
  const formRef = useRef(null)

  const scrollToForm = () => {
    formRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  const handleDragOver = (e) => { e.preventDefault(); setIsDragging(true) }
  const handleDragLeave = () => setIsDragging(false)

  const handleDrop = (e) => {
    e.preventDefault()
    setIsDragging(false)
    const dropped = e.dataTransfer.files?.[0]
    if (dropped?.type === 'application/pdf') {
      setFile(dropped); setError('')
    } else {
      setError('Por favor, envie apenas ficheiros PDF.')
    }
  }

  const handleFileChange = (e) => {
    if (e.target.files?.[0]) { setFile(e.target.files[0]); setError('') }
  }

  const clearFile = (e) => {
    e.stopPropagation()
    setFile(null)
    setError('')
    if (fileInputRef.current) fileInputRef.current.value = ''
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!file || !vaga) {
      setError('Anexe seu currículo e preencha a descrição da vaga.')
      return
    }
    setLoading(true); setError(''); setSuccess(false)

    const formData = new FormData()
    formData.append('arquivo', file)
    formData.append('vaga', vaga)

    try {
      const response = await fetch('https://ats-backend-api-o8h8.onrender.com/api/curriculo/analisar', {
        method: 'POST',
        body: formData,
      })
      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(errorText || 'Erro no servidor.')
      }
      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = 'Curriculo_Otimizado.pdf'
      document.body.appendChild(a)
      a.click()
      a.remove()
      window.URL.revokeObjectURL(url)
      setSuccess(true)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="page">
      {/* ── HERO ── */}
      <section className="hero">
        <div className="hero-bg-grid" aria-hidden="true" />
        <div className="hero-glow" aria-hidden="true" />

        <nav className="nav">
          <div className="nav-logo">
            <Sparkles size={20} />
            <span>ATS Optimizer</span>
          </div>
          <button className="nav-cta" onClick={scrollToForm}>
            Começar grátis
          </button>
        </nav>

        <div className="hero-content">
          <div className="badge">
            <Zap size={14} />
            Powered by AI
          </div>
          <h1 className="hero-title">
            Seu currículo,<br />
            <span className="gradient-text">irresistível para ATS</span>
          </h1>
          <p className="hero-subtitle">
            Cole a descrição da vaga, faça upload do seu PDF e receba em segundos
            um currículo reescrito pela IA — otimizado para passar em qualquer
            sistema de triagem automática.
          </p>
          <div className="hero-actions">
            <button className="btn-primary" onClick={scrollToForm}>
              <Sparkles size={18} />
              Otimizar meu currículo
            </button>
          </div>

          <div className="hero-stats">
            {['98% de aprovação', '< 30 segundos', 'Grátis para usar'].map((s) => (
              <div key={s} className="stat-pill">
                <CheckCircle2 size={14} />
                {s}
              </div>
            ))}
          </div>
        </div>

        <button className="scroll-hint" onClick={scrollToForm} aria-label="Rolar para baixo">
          <ChevronDown size={20} />
        </button>
      </section>

      {/* ── FEATURES ── */}
      <section className="features">
        <div className="section-label">Como funciona</div>
        <h2 className="section-title">Tudo que você precisa para<br />se destacar nas seleções</h2>
        <div className="features-grid">
          {FEATURES.map((f) => (
            <div key={f.title} className="feature-card">
              <div className="feature-icon">{f.icon}</div>
              <h3>{f.title}</h3>
              <p>{f.desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* ── FORM ── */}
      <section className="form-section" ref={formRef}>
        <div className="form-card">
          <div className="form-card-header">
            <div className="form-logo">
              <Sparkles size={22} color="#fff" />
            </div>
            <div>
              <h2>Gerar Currículo Otimizado</h2>
              <p>Preencha os campos abaixo e deixe a IA trabalhar</p>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="form-body">
            {/* Upload */}
            <div className="field-wrap">
              <label className="field-label">
                <UploadCloud size={16} />
                Seu currículo atual <span className="required">*</span>
              </label>
              <div
                id="drop-zone"
                className={`drop-zone ${isDragging ? 'dragging' : ''} ${file ? 'has-file' : ''}`}
                onDragOver={handleDragOver}
                onDragLeave={handleDragLeave}
                onDrop={handleDrop}
                onClick={() => fileInputRef.current?.click()}
                role="button"
                tabIndex={0}
                onKeyDown={(e) => e.key === 'Enter' && fileInputRef.current?.click()}
              >
                <input
                  type="file"
                  ref={fileInputRef}
                  accept="application/pdf"
                  onChange={handleFileChange}
                  style={{ display: 'none' }}
                />
                {file ? (
                  <div className="file-success">
                    <CheckCircle2 size={36} className="icon-success" />
                    <div className="file-info">
                      <span className="file-name">{file.name}</span>
                      <span className="file-size">{(file.size / 1024).toFixed(1)} KB</span>
                    </div>
                    <button
                      type="button"
                      className="file-remove"
                      onClick={clearFile}
                      aria-label="Remover arquivo"
                    >
                      <X size={16} />
                    </button>
                  </div>
                ) : (
                  <div className="drop-idle">
                    <UploadCloud size={40} className="drop-icon" />
                    <p><strong>Clique para selecionar</strong> ou arraste o PDF aqui</p>
                    <span className="hint">Somente PDF · Máx. 10 MB</span>
                  </div>
                )}
              </div>
            </div>

            {/* Vaga */}
            <div className="field-wrap">
              <label htmlFor="vaga" className="field-label">
                <FileText size={16} />
                Descrição da vaga alvo <span className="required">*</span>
              </label>
              <textarea
                id="vaga"
                rows={6}
                placeholder="Cole aqui os requisitos, responsabilidades e tecnologias da vaga (ex: React, Node.js, TypeScript...)"
                value={vaga}
                onChange={(e) => setVaga(e.target.value)}
              />
              <span className="char-count">{vaga.length} caracteres</span>
            </div>

            {/* Feedback */}
            {error && (
              <div className="alert alert-error" role="alert">
                <AlertCircle size={18} />
                <span>{error}</span>
              </div>
            )}
            {success && (
              <div className="alert alert-success" role="status">
                <CheckCircle2 size={18} />
                <span>Currículo otimizado baixado com sucesso! 🎉</span>
              </div>
            )}

            <button
              type="submit"
              id="submit-btn"
              className="btn-submit"
              disabled={loading || !file || !vaga}
            >
              {loading ? (
                <>
                  <span className="spinner" />
                  Analisando com IA...
                </>
              ) : (
                <>
                  <Sparkles size={18} />
                  Gerar Currículo Otimizado
                </>
              )}
            </button>
          </form>
        </div>
      </section>

      {/* ── FOOTER ── */}
      <footer className="footer">
        <Sparkles size={16} />
        <span>ATS Optimizer · Feito com IA para você conseguir a vaga dos seus sonhos</span>
      </footer>
    </div>
  )
}

export default App