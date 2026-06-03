import React, { useState, useRef, useEffect } from "react";

/* ─── Inline styles (no extra CSS file needed) ─────────────────────────────── */
const S = {
    root: {
        display: "flex",
        flexDirection: "column",
        height: "100vh",
        maxWidth: 560,
        margin: "0 auto",
        background: "#f5f4f0",
        fontFamily: "'Georgia', serif",
        overflow: "hidden",
    },
    header: {
        padding: "18px 22px 14px",
        background: "#1a1a2e",
        display: "flex",
        alignItems: "center",
        gap: 10,
        flexShrink: 0,
        boxShadow: "0 2px 12px rgba(0,0,0,0.25)",
    },
    headerDot: {
        width: 9,
        height: 9,
        borderRadius: "50%",
        background: "#e2c27d",
        boxShadow: "0 0 8px #e2c27d",
    },
    headerTitle: {
        fontFamily: "'Georgia', serif",
        fontWeight: "bold",
        fontSize: 15,
        color: "#f0eee8",
        letterSpacing: "0.03em",
    },
    headerSub: {
        marginLeft: "auto",
        fontSize: 11,
        color: "#7c7a8a",
        fontFamily: "monospace",
    },
    messages: {
        flex: 1,
        overflowY: "auto",
        padding: "28px 18px 16px",
        display: "flex",
        flexDirection: "column",
        gap: 14,
    },
    /* Bot bubble */
    botRow: {
        display: "flex",
        alignItems: "flex-end",
        gap: 8,
        alignSelf: "flex-start",
        maxWidth: "78%",
    },
    avatar: {
        width: 30,
        height: 30,
        borderRadius: "50%",
        background: "#1a1a2e",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontSize: 14,
        flexShrink: 0,
    },
    botBubble: {
        background: "#ffffff",
        border: "1px solid #e0ddd6",
        borderRadius: "18px 18px 18px 4px",
        padding: "11px 15px",
        fontSize: 13.5,
        lineHeight: 1.65,
        color: "#1c1c1c",
        boxShadow: "0 1px 4px rgba(0,0,0,0.07)",
        whiteSpace: "pre-wrap",
    },
    /* User bubble */
    userRow: {
        display: "flex",
        justifyContent: "flex-end",
        alignSelf: "flex-end",
        maxWidth: "72%",
    },
    userBubble: {
        background: "#1a1a2e",
        borderRadius: "18px 18px 4px 18px",
        padding: "11px 15px",
        fontSize: 13.5,
        lineHeight: 1.65,
        color: "#f0eee8",
        boxShadow: "0 1px 6px rgba(26,26,46,0.25)",
    },
    /* Option chips */
    optionChips: {
        display: "flex",
        flexDirection: "column",
        gap: 7,
        marginTop: 10,
    },
    chip: {
        display: "flex",
        alignItems: "center",
        gap: 9,
        background: "#f8f6f0",
        border: "1.5px solid #d6d2c8",
        borderRadius: 12,
        padding: "9px 13px",
        fontSize: 13,
        cursor: "pointer",
        transition: "all 0.18s",
        color: "#1c1c1c",
        fontFamily: "'Georgia', serif",
        textAlign: "left",
    },
    chipDisabled: {
        opacity: 0.45,
        cursor: "not-allowed",
    },
    chipBadge: {
        background: "#1a1a2e",
        color: "#e2c27d",
        borderRadius: 6,
        padding: "2px 7px",
        fontSize: 11,
        fontFamily: "monospace",
        fontWeight: "bold",
        flexShrink: 0,
    },
    /* Roadmap */
    roadmapWrap: {
        marginTop: 10,
        display: "flex",
        flexDirection: "column",
        gap: 0,
    },
    roadmapStep: {
        display: "flex",
        gap: 11,
        alignItems: "flex-start",
    },
    stepLeft: {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        flexShrink: 0,
    },
    stepNum: {
        width: 24,
        height: 24,
        borderRadius: "50%",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontSize: 11,
        fontFamily: "monospace",
        fontWeight: "bold",
        border: "2px solid",
        flexShrink: 0,
    },
    stepConnector: {
        width: 2,
        height: 18,
        background: "#d6d2c8",
        margin: "2px 0",
    },
    stepBody: {
        paddingBottom: 14,
        paddingTop: 2,
        flex: 1,
    },
    stepTask: {
        fontSize: 12.5,
        color: "#1c1c1c",
        lineHeight: 1.5,
        fontWeight: "bold",
    },
    stepReason: {
        fontSize: 11.5,
        color: "#6b6860",
        marginTop: 2,
        lineHeight: 1.4,
        fontStyle: "italic",
    },
    /* Typing */
    typingBubble: {
        background: "#ffffff",
        border: "1px solid #e0ddd6",
        borderRadius: "18px 18px 18px 4px",
        padding: "13px 16px",
        display: "flex",
        gap: 5,
        alignItems: "center",
        width: "fit-content",
        boxShadow: "0 1px 4px rgba(0,0,0,0.07)",
    },
    dot: {
        width: 7,
        height: 7,
        borderRadius: "50%",
        background: "#aaa9a2",
    },
    /* Input */
    inputArea: {
        padding: "12px 16px",
        borderTop: "1px solid #dddad3",
        background: "#ffffff",
        display: "flex",
        gap: 9,
        alignItems: "center",
        flexShrink: 0,
    },
    textInput: {
        flex: 1,
        border: "1.5px solid #d6d2c8",
        borderRadius: 22,
        padding: "9px 16px",
        fontSize: 13.5,
        fontFamily: "'Georgia', serif",
        outline: "none",
        background: "#faf9f6",
        color: "#1c1c1c",
        transition: "border-color 0.2s",
    },
    sendBtn: {
        width: 38,
        height: 38,
        borderRadius: "50%",
        background: "#1a1a2e",
        border: "none",
        cursor: "pointer",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        flexShrink: 0,
        transition: "transform 0.15s, background 0.15s",
    },
    nameRow: {
        padding: "12px 16px",
        borderTop: "1px solid #dddad3",
        background: "#ffffff",
        display: "flex",
        gap: 9,
        alignItems: "center",
        flexShrink: 0,
    },
    nameLabel: {
        fontSize: 11,
        color: "#8a8880",
        fontFamily: "monospace",
        textTransform: "uppercase",
        letterSpacing: "0.07em",
        whiteSpace: "nowrap",
    },
};

const STEP_COLORS = ["#1a1a2e", "#3d5a80", "#5c7a3e", "#8b4513", "#6b3a6b", "#2e5b5b"];

/* ─── Typing dots ───────────────────────────────────────────────────────────── */
function TypingDots() {
    return (
        <div style={S.botRow}>
            <div style={S.avatar}>🤖</div>
            <div style={S.typingBubble}>
                {[0, 1, 2].map((i) => (
                    <div
                        key={i}
                        style={{
                            ...S.dot,
                            animation: `taskbot-bounce 1.1s ${i * 0.18}s infinite`,
                        }}
                    />
                ))}
            </div>
        </div>
    );
}

/* ─── Roadmap card ──────────────────────────────────────────────────────────── */
function RoadmapCard({ steps }) {
    if (!steps?.length) return null;
    return (
        <div style={S.roadmapWrap}>
            {steps.map((s, i) => (
                <div key={i} style={S.roadmapStep}>
                    <div style={S.stepLeft}>
                        <div
                            style={{
                                ...S.stepNum,
                                borderColor: STEP_COLORS[i % STEP_COLORS.length],
                                color: STEP_COLORS[i % STEP_COLORS.length],
                            }}
                        >
                            {s.order}
                        </div>
                        {i < steps.length - 1 && <div style={S.stepConnector} />}
                    </div>
                    <div style={S.stepBody}>
                        <div style={S.stepTask}>{s.task}</div>
                        {s.reason && <div style={S.stepReason}>{s.reason}</div>}
                    </div>
                </div>
            ))}
        </div>
    );
}

/* ─── Option chips ──────────────────────────────────────────────────────────── */
function OptionChips({ onSelect }) {
    const [hovered, setHovered] = useState(null);
    return (
        <div style={S.optionChips}>
            <button
                style={{
                    ...S.chip,
                    ...(hovered === "A"
                        ? { background: "#1a1a2e", color: "#f0eee8", borderColor: "#1a1a2e" }
                        : {}),
                }}
                onMouseEnter={() => setHovered("A")}
                onMouseLeave={() => setHovered(null)}
                onClick={() => onSelect("A")}
            >
                <span style={{ ...S.chipBadge, background: hovered === "A" ? "#e2c27d" : "#1a1a2e", color: hovered === "A" ? "#1a1a2e" : "#e2c27d" }}>A</span>
                See my suggested roadmap
            </button>
            <button
                style={{ ...S.chip, ...S.chipDisabled }}
                disabled
            >
                <span style={S.chipBadge}>B</span>
                User report
                <span style={{ marginLeft: 6, fontSize: 11, color: "#9a9890", fontStyle: "italic" }}>
          coming soon
        </span>
            </button>
        </div>
    );
}

/* ─── Single message ────────────────────────────────────────────────────────── */
function Message({ msg, username }) {
    if (msg.role === "user") {
        return (
            <div style={{ ...S.userRow, animation: "taskbot-slideR 0.25s ease" }}>
                <div style={S.userBubble}>{msg.text}</div>
            </div>
        );
    }
    return (
        <div style={{ ...S.botRow, flexDirection: "column", alignItems: "flex-start" }}>
            <div style={{ display: "flex", alignItems: "flex-end", gap: 8 }}>
                <div style={S.avatar}>🤖</div>
                <div style={{ ...S.botBubble, animation: "taskbot-slideL 0.25s ease" }}>
                    {msg.text}
                </div>
            </div>
            {msg.chips && (
                <div style={{ marginLeft: 38 }}>
                    <OptionChips onSelect={msg.chips} />
                </div>
            )}
            {msg.roadmap && (
                <div style={{ marginLeft: 38, marginTop: 8, maxWidth: 400 }}>
                    <RoadmapCard steps={msg.roadmap} />
                </div>
            )}
        </div>
    );
}

/* ─── Main component ────────────────────────────────────────────────────────── */
export default function AIChatbot() {
    const [phase, setPhase] = useState("name"); // "name" | "chat"
    const [username, setUsername] = useState("");
    const [nameInput, setNameInput] = useState("");
    const [messages, setMessages] = useState([]);
    const [inputText, setInputText] = useState("");
    const [loading, setLoading] = useState(false);
    const bottomRef = useRef(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages, loading]);

    /* ── helpers ── */
    function addMsg(role, text, extras = {}) {
        setMessages((prev) => [...prev, { role, text, id: Date.now() + Math.random(), ...extras }]);
    }

    /* ── start chat ── */
    /* ── start chat ── */
    async function startChat() {
        const name = nameInput.trim();
        if (!name) return;
        setUsername(name);
        setPhase("chat");
        setLoading(true);

        try {
            const res = await fetch(`/ai/greet?username=${encodeURIComponent(name)}`);
            const data = await res.json();
            setLoading(false);
            addMsg("bot", data.message, {
                chips: (opt) => handleOption(opt, name),  // ← pass name here
            });
        } catch {
            setLoading(false);
            addMsg("bot", `Hello ${name}! What would you like to do?`, {
                chips: (opt) => handleOption(opt, name)   // ← and here
            });
        }
    }

    /* ── option A / B ── */
    async function handleOption(opt, usernameOverride) {
        if (opt !== "A") return;
        const resolvedUsername = usernameOverride || username;  // ← use override
        addMsg("user", "A — Show my roadmap");
        setLoading(true);

        try {
            const res = await fetch("/ai/chat", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username: resolvedUsername, option: "A" }),  // ← use resolvedUsername
            });
            const data = await res.json();
            setLoading(false);
            addMsg("bot", data.reply || "Here's your roadmap:", { roadmap: data.roadmap });
            setTimeout(() => {
                addMsg("bot", "Anything else?", { chips: (opt) => handleOption(opt, resolvedUsername) });  // ← pass it along
            }, 500);
        } catch {
            setLoading(false);
            addMsg("bot", "Couldn't reach the server. Please try again.");
        }
    }

    /* ── free text ── */
    async function sendFreeText() {
        const text = inputText.trim();
        if (!text || loading) return;
        setInputText("");
        addMsg("user", text);
        setLoading(true);

        try {
            const res = await fetch("/ai/chat", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, freeText: text }),
            });
            const data = await res.json();
            setLoading(false);
            addMsg("bot", data.reply);
        } catch {
            setLoading(false);
            addMsg("bot", "Something went wrong. Please try again.");
        }
    }

    /* ── render ── */
    return (
        <>
            <style>{`
        @keyframes taskbot-bounce {
          0%,60%,100% { transform: translateY(0); background: #aaa9a2; }
          30% { transform: translateY(-5px); background: #1a1a2e; }
        }
        @keyframes taskbot-slideL {
          from { opacity:0; transform: translateX(-8px); }
          to   { opacity:1; transform: translateX(0); }
        }
        @keyframes taskbot-slideR {
          from { opacity:0; transform: translateX(8px); }
          to   { opacity:1; transform: translateX(0); }
        }
        .taskbot-chip-hover:hover { background:#1a1a2e !important; color:#f0eee8 !important; border-color:#1a1a2e !important; }
        .taskbot-send:hover { background:#3d3d6e !important; transform: scale(1.07); }
        .taskbot-input:focus { border-color: #1a1a2e !important; }
        ::-webkit-scrollbar { width: 4px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: #d6d2c8; border-radius: 4px; }
      `}</style>

            <div style={S.root}>
                {/* Header */}
                <div style={S.header}>
                    <div style={S.headerDot} />
                    <div style={S.headerTitle}>TaskBot</div>
                    <div style={S.headerSub}>AI assistant</div>
                </div>

                {/* Messages */}
                <div style={S.messages}>
                    {messages.map((msg) => (
                        <Message key={msg.id} msg={msg} username={username} />
                    ))}
                    {loading && <TypingDots />}
                    <div ref={bottomRef} />
                </div>

                {/* Name entry */}
                {phase === "name" && (
                    <div style={S.nameRow}>
                        <span style={S.nameLabel}>name:</span>
                        <input
                            className="taskbot-input"
                            style={S.textInput}
                            placeholder="Enter your name…"
                            value={nameInput}
                            onChange={(e) => setNameInput(e.target.value)}
                            onKeyDown={(e) => e.key === "Enter" && startChat()}
                            autoFocus
                        />
                        <button
                            className="taskbot-send"
                            style={S.sendBtn}
                            onClick={startChat}
                        >
                            <svg width="15" height="15" viewBox="0 0 24 24" fill="#e2c27d">
                                <path d="M5 3l14 9-14 9V3z" />
                            </svg>
                        </button>
                    </div>
                )}

                {/* Chat input */}
                {phase === "chat" && (
                    <div style={S.inputArea}>
                        <input
                            className="taskbot-input"
                            style={S.textInput}
                            placeholder="Ask me about your tasks…"
                            value={inputText}
                            onChange={(e) => setInputText(e.target.value)}
                            onKeyDown={(e) => e.key === "Enter" && sendFreeText()}
                            disabled={loading}
                        />
                        <button
                            className="taskbot-send"
                            style={{ ...S.sendBtn, opacity: loading ? 0.45 : 1 }}
                            onClick={sendFreeText}
                            disabled={loading}
                        >
                            <svg width="15" height="15" viewBox="0 0 24 24" fill="#e2c27d">
                                <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" />
                            </svg>
                        </button>
                    </div>
                )}
            </div>
        </>
    );
}