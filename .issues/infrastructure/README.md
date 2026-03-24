# INFRASTRUCTURE ISSUES - SocialSeed

## Issue Summary
Docker, Kubernetes, and infrastructure configuration issues.

---

## DOCKER COMPOSE

### [DOCKER-001] api-gateway Service Missing
**Severity:** CRITICAL  
**File:** `docker-compose.yml`

**Problem:** api-gateway service defined in `pom.xml` but not in `docker-compose.yml`.

**Impact:** API Gateway cannot be deployed.

---

### [DOCKER-002] Kafka Consumers Disabled
**Severity:** CRITICAL  
**File:** `services/socialuser-service/src/main/resources/application.yml`

**Problem:**
```yaml
kafka:
  listener:
    auto-startup: false
```

**Impact:** socialuser-service won't consume Kafka events.

---

### [DOCKER-003] Neo4j Dependency Commented Out
**Severity:** HIGH  
**File:** `docker-compose.yml`  
**Lines:** 31-33

**Problem:** socialuser-service can start before Neo4j is ready.

**Recommendation:** Uncomment and fix:
```yaml
depends_on:
  neo4j:
    condition: service_healthy
```

---

### [DOCKER-004] Redis Dependency Missing
**Severity:** HIGH  
**File:** `docker-compose.yml`

**Problem:** auth-service requires Redis but no dependency defined.

---

### [DOCKER-005] Missing Healthchecks
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Missing healthchecks for:**
- api-gateway
- auth-service

---

## PORTS & NETWORKING

### [DOCKER-010] API Gateway Route Port Mismatch
**Severity:** CRITICAL  
**File:** `services/api-gateway/src/main/resources/application.yml`

**Problem:**
```yaml
routes:
  - id: socialuser-route
    uri: http://socialuser-service:4000  # WRONG - should be 8090
```

---

### [DOCKER-011] Windows-Specific Volume Paths
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:**
```yaml
volumes:
  - D:/db_volumes/neo4j_data:/var/lib/neo4j/data
  - D:/db_volumes/postgres_data:/var/lib/postgresql/data
  - D:/db_volumes/kafka_data:/var/lib/kafka/data
```

**Recommendation:** Use relative paths:
```yaml
volumes:
  - ./infrastructure/volumes/neo4j_data:/var/lib/neo4j/data
```

---

### [DOCKER-012] No Redis Persistence
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** Redis data will be lost on container recreation.

---

## DOCKERFILE

### [DOCKER-020] Dockerfile Build Context Inconsistency
**Severity:** LOW  
**Files:**
- `services/api-gateway/Dockerfile` - Single service context
- `services/auth-service/Dockerfile` - Root context

**Problem:** Different patterns between services.

---

### [DOCKER-021] gRPC Port Not Exposed
**Severity:** LOW  
**File:** `services/socialuser-service/Dockerfile`

**Problem:** Only exposes 8090 (REST), but gRPC runs on 9090.

---

## KAFKA

### [KAFKA-001] Topics Not Initialized in Docker
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** `infrastructure/kafka/topics-init.sh` not mounted or executed.

---

### [KAFKA-002] No Kafka UI
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** No development tool for Kafka debugging.

**Recommendation:** Add:
```yaml
kafka-ui:
  image: provectuslabs/kafka-ui:latest
  ports:
    - "8099:8080"
  environment:
    KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
```

---

### [KAFKA-003] No Schema Registry
**Severity:** LOW  
**File:** `docker-compose.yml`

**Problem:** No Confluent Schema Registry for Kafka message evolution.

---

## RESOURCE MANAGEMENT

### [DOCKER-030] No Resource Limits
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** No memory or CPU limits configured.

**Recommendation:** Add to each service:
```yaml
deploy:
  resources:
    limits:
      memory: 512M
```

---

## MISSING SERVICES

### [DOCKER-040] Services Listed but Not Active
**Severity:** MEDIUM  
**File:** `pom.xml`

**Commented Out:**
- api-gateway
- governance-service
- post-service
- reactions-service
- relationship-service

---

### [DOCKER-041] api-gateway Dockerfile Exists But Not Used
**Severity:** LOW  
**Files:**
- `services/api-gateway/Dockerfile` exists
- But service not defined in docker-compose.yml

---

## KUBERNETES (Future)

### [K8S-001] No Kubernetes Manifests
**Severity:** LOW  
**Location:** `infrastructure/kubernetes/`

**Problem:** No K8s deployment configurations.

---

## MONITORING

### [MON-001] No Monitoring Stack
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Missing:**
- Prometheus
- Grafana
- Jaeger (Tracing)
- ELK Stack

---

## Priority Order
1. DOCKER-001 (api-gateway missing)
2. DOCKER-002 (Kafka consumers disabled)
3. DOCKER-003, DOCKER-004 (dependencies)
4. DOCKER-010 (port mismatch)
5. DOCKER-005, DOCKER-030 (reliability)
