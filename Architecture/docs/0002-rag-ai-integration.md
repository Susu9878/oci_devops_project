# ADR 0002: Adopt Retrieval-Augmented Generation (RAG) with Oracle Generative AI Services

## Status
Accepted

## Context

STAMP provides AI-assisted sprint planning and task prioritization capabilities through the Sky-Tec telegram assistant chatbot.

The platform required an AI integration strategy capable of:
- generating contextual recommendations
- reducing hallucinations
- incorporating sprint and task information
- supporting future enterprise knowledge integration
- maintaining provider flexibility

The system processes:
- active sprint tasks
- task priorities
- story points
- expected hours
- task descriptions
- KPI metrics

A Retrieval-Augmented Generation (RAG) approach was selected to improve contextual understanding and recommendation quality.

---

## Decision

STAMP will adopt Retrieval-Augmented Generation (RAG) using Oracle Generative AI Services as the primary inference provider.

The AI architecture includes:
- retrieval services
- prompt construction
- contextual task enrichment
- AI provider abstraction
- fallback provider support

The system uses Oracle Generative AI Services as the primary AI provider and maintains Claude integration as a secondary fallback provider.

---

## Architecture Overview

The AI workflow consists of:

1. Retrieve active sprint and task information from Oracle Autonomous Database
2. Build contextual prompts using sprint metadata
3. Enrich prompts through RAG orchestration
4. Send requests to Oracle Generative AI Services
5. Generate AI-assisted roadmap recommendations
6. Return contextual responses to the user

---

## Consequences

### Positive Consequences
- More contextual AI recommendations
- Reduced hallucinations
- Better sprint prioritization accuracy
- Easier future enterprise knowledge integration
- Provider abstraction improves flexibility
- Improved maintainability of AI integrations

### Negative Consequences
- Increased architectural complexity
- Additional orchestration logic required
- Greater infrastructure dependency on OCI AI services

---

## Related Views
- System Context Diagram
- AI Component Diagram
- Dynamic AI Recommendation Flow
