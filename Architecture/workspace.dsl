workspace "STAMP - Sky Tec Assisted Management Program" "AI-assisted sprint and task management platform deployed on OCI with RAG integration." {

    !adrs docs

    model {

        user = person "Developer/User" "Uses STAMP to manage tasks, sprints, and AI-assisted roadmap generation."

        admin = person "Administrator" "Manages users, tasks, sprint assignments, task prioritization, and overall platform operations."

        stamp = softwareSystem "STAMP" "Sky Tec Assisted Management Program. AI-assisted sprint and task management platform." {

            frontend = container "React Frontend" "Provides the user interface for sprint management and AI interactions." "React"

            backend = container "Spring Boot Backend" "Core business logic, REST APIs, authentication, KPI management, and AI orchestration." "Spring Boot" {

                controller = component "Controllers" "REST API endpoints for tasks, users, KPIs, and AI interactions." "Spring REST Controllers"

                service = component "Services" "Business logic and application orchestration." "Spring Services"

                repository = component "Repositories" "Database access layer." "Spring Data JPA"

                security = component "Security Layer" "Authentication and authorization handling." "Spring Security"

                aiadapter = component "AI Provider Adapter Layer" "Abstracts AI provider integrations and fallback providers." "Adapter Layer"

                controller -> service "Invokes business logic"
                service -> repository "Reads/writes entities"
                service -> security "Validates authentication and permissions"
                service -> aiadapter "Requests AI orchestration"
            }

            rag = container "RAG Orchestrator" "Handles contextual retrieval, prompt enrichment, and AI orchestration workflows." "Java Service" {

                ragservice = component "Retrieval Service" "Retrieves contextual sprint and task information for RAG workflows."

                promptbuilder = component "Prompt Builder" "Builds enriched prompts for AI processing."

                oracleadapter = component "Oracle AI Adapter" "Primary Oracle Generative AI integration."

                claudefallback = component "Claude Fallback Adapter" "Fallback AI provider integration."

                ragservice -> promptbuilder "Builds enriched prompts"
            }

            db = container "Oracle Autonomous Database" "Stores users, tasks, sprints, KPIs, and application data." "Oracle Autonomous Database"

            frontend -> backend "Sends REST API requests" "HTTPS/JSON"
            backend -> db "Reads and writes application data" "JPA/JDBC"
            backend -> rag "Requests AI-assisted prioritization and roadmap generation"
            aiadapter -> ragservice "Requests contextual retrieval"
        }

        oracleai = softwareSystem "Oracle Generative AI Services" "Primary AI provider used for RAG-based roadmap generation."

        claude = softwareSystem "Claude AI API" "Fallback AI provider used for secondary inference support."

        backend -> oracleai "Sends enriched AI prompts"
        backend -> claude "Fallback AI requests"

        user -> frontend "Uses"
        admin -> stamp "Monitors and manages"

        rag -> oracleai "Primary AI inference requests"
        rag -> claude "Fallback inference requests"
        rag -> db "Retrieves contextual sprint and task data"

        production = deploymentEnvironment "Production" {

            deploymentNode "Oracle Cloud Infrastructure" "OCI Cloud Environment" {

                deploymentNode "OKE Cluster" "Oracle Kubernetes Engine" {

                    deploymentNode "Frontend Pod" {
                        containerInstance frontend
                    }

                    deploymentNode "Backend Pod" {
                        containerInstance backend
                        containerInstance rag
                    }

                    deploymentNode "OCI Load Balancer" {
                    }
                }

                deploymentNode "Oracle Autonomous Database" {
                    containerInstance db
                }

                deploymentNode "Oracle Generative AI Services" {
                }
            }
        }
    }

    views {

        systemLandscape "system-landscape" {
            include *
            autoLayout lr
            title "STAMP - System Landscape"
            description "High-level overview of STAMP and external systems."
        }

        systemContext stamp "system-context" {
            include *
            autoLayout lr
            title "STAMP - System Context"
            description "System context view for STAMP."
        }

        container stamp "containers" {
            include *
            autoLayout lr
            title "STAMP - Container Diagram"
            description "Container-level architecture for STAMP."
        }

        component backend "backend-components" {
            include *
            autoLayout lr
            title "STAMP - Backend Components"
            description "Internal backend component structure."
        }

        component rag "rag-components" {
            include *
            autoLayout lr
            title "STAMP - RAG Components"
            description "RAG orchestration and AI integration components."
        }

        deployment stamp "Production" "deployment" {
            include *
            autoLayout lr
            title "STAMP - Deployment Diagram"
            description "Cloud-native deployment architecture on OCI."
        }

        dynamic stamp "ai-prioritization-flow" {
            title "AI-Assisted Sprint Prioritization Flow"

            user -> frontend "Requests AI roadmap generation"
            frontend -> backend "POST /ai/chat"
            backend -> db "Retrieves active sprint tasks"
            backend -> rag "Sends contextual task information"
            rag -> db "Retrieves additional contextual data"
            rag -> oracleai "Generates prioritized roadmap"
            oracleai -> rag "Returns AI recommendations"
            rag -> backend "Returns enriched roadmap"
            backend -> frontend "Returns AI response"
            frontend -> user "Displays roadmap and prioritization"

            autoLayout lr
        }

        styles {
            element "Person" {
                shape Person
                background #08427b
                color #ffffff
            }

            element "Software System" {
                background #1168bd
                color #ffffff
            }

            element "Container" {
                background #438dd5
                color #ffffff
            }

            element "Component" {
                background #85bbf0
                color #000000
            }

            element "Database" {
                shape Cylinder
            }
        }

        theme default
    }
}