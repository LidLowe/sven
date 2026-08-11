# SVEN - Solana Verification Engine 🛡️

SVEN is an open-source modular Spring Boot starter that brings native Web3 
authentication and token-gating to Java applications - without SaaS dependencies 
or JS proxies.

> Web3 auth tooling is built almost exclusively for JS/TS and Rust.
> SVEN eliminates this gap by bringing self-hosted, cryptographic wallet 
> authentication directly into the Spring Boot ecosystem.

> 🚧 **Status: Active Development**
> L1 Verification (Ed25519) and core architecture are implemented.
> L2/L3 verification, Spring Security autoconfiguration, and Telegram module are in progress.

---

## How It Works

1. User sends a public key → SVEN generates a `Nonce` (cached with TTL)
2. User signs the Nonce in their wallet (e.g. Phantom) and returns it with the signature
3. SVEN verifies the Ed25519 signature natively via `java.security` (L1)
4. SVEN queries on-chain assets (L2) and account state (L3) via RPC - skipped on cache hit
5. Result passed to `AuthenticationProvider` → JWT issued
6. `VerificationSuccessEvent` published → handlers fire (Telegram, Discord, webhooks...)

---

## Architecture

Multi-module Maven project:

**`sven-core`** - main verification engine
- L1: Ed25519 signature validation via native Java 21 `java.security`
- L2: On-chain asset checks - SPL tokens, Metaplex NFT, Token-2022 *(in progress)*
- L3: Account state filters - SOL balance, wallet age, blacklists *(in progress)*
- Spring Security: autoconfigured filter chain, custom `AuthenticationProvider`, JWT generation
- Cache: hybrid Caffeine / Redis with configurable TTL via `SvenProperties`
- Events: `VerificationSuccessEvent` published to Spring Application Context

**`sven-telegram-spring-boot-starter`** - optional add-on *(planned)*
- Telegram token-gating via one-time invite links
- Mini Apps session authorization

---

## Tech Stack

- Java 21
- Spring Boot 4.1.x / Spring Security 7.x
- SolanaJ (RPC communication)
- Caffeine / Redis (hybrid caching)
- Nimbus-Jose-JWT

---

## Quick Start

> Maven Central publication is scheduled for Milestone 4.
> Currently, clone and build locally.

**1. Build locally**
```bash
git clone https://github.com/LidLowe/sven
cd sven
mvn clean install -DskipTests
```

**2. Add dependency**
```xml
<dependency>
    <groupId>io.github.lidlowe</groupId>
    <artifactId>sven-core</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

**3. Configure `application.yml`**
```yaml
sven:
  rpc: "https://api.devnet.solana.com"
  cache:
    type: caffeine
    ttl-seconds: 300
  jwt:
    secret: "your-secret-key"
    expiration-seconds: 3600
```

---

## Roadmap

- [x] M1: Core architecture, L1 verification (Ed25519), Nonce generation
- [ ] M2: L2/L3 verification, Caffeine/Redis cache, event publishing
- [ ] M3: Telegram starter, event adapters
- [ ] M4: Maven Central release, CI/CD, documentation

---

## License

MIT

---

## Author

**Margulan Zhaskairatuly**
- Telegram: [@LidLowe](https://t.me/LidLowe)
