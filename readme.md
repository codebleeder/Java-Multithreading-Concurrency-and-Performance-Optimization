Why we need threads:
* Responsiveness
* Performance

* process will not terminate if there is at least one normal thread still executing.
* A process can terminate if there is a daemon thread. Ex: file saving thread in a text editor
* Thread-pooling: re-using threads instead of creating new threads repeatedly
* Atomic operation: an operation/set of operations that appears as if it occurred once to the rest of the system; has no intermediate states; appears as one step

Atomic operations:
* primitive assignments, assignments to references
* assignment to double and long using 'volatile' keyword