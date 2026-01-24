# Issue Summary: Cleanup Expired Tokens Job (#93)

## Overview
Implemented an automated cleaning mechanism for the `refresh_tokens` table.

## Implementation Details
- **Job Name**: `TokenCleanupJob`
- **Schedule**: User-configurable via `auth.job.cleanup-cron` (default: `0 0 0 * * ?` - Daily at midnight).
- **Operation**: Deletes all refresh tokens where `expiry_date` < `Instant.now()`.

## Database Impact
- **Query**: `DELETE FROM refresh_tokens WHERE expiry_date < ?`
- **Performance**: This job runs in a transaction. For databases with millions of expired tokens, expected initial run time might be higher. Subsequent runs will be fast.

## Configuration
- `auth.job.cleanup-cron`: Cron expression for the job schedule.
