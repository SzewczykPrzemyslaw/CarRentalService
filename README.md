Limitations and trade-offs:

Pessimistic locking: 
Because FOR UPDATE locks are held for the entire @Transactional operation, it may happen that transaction holds the lock of multiple candidate cars when previously selected cars turns out to be unavailable, this may lead to multiple requests blocked waiting for this transaction to unrelease the lock under high concurrency. This can become a throughput issue as the system potentially scales.

Candidate selection:
Availability query may return many candidates every request in case of large dataset of cars which leads to unnecessary database and memory usage. A bounded result with 'LIMIT' would reduce the cost, but may miss other available cars if all returned candidates become unavailable concurrently. Current implementation prioritizes correctness, but production system could process candidates in batches.
