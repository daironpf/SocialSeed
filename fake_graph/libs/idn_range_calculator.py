import os

class IDNRangeCalculator:
    @staticmethod
    def calculate_ranges(total: int) -> list:
        batch_size = IDNRangeCalculator._calculate_batch_size(total)
        idn_ranges = []
        id_jump = -batch_size

        for _ in range(int(total / batch_size) - 1):
            id_jump = batch_size + id_jump + 1
            if id_jump == total:
                element = [id_jump, id_jump]
                idn_ranges.append(element)
                return idn_ranges

            element = [id_jump, batch_size + id_jump]
            idn_ranges.append(element)

            if batch_size + id_jump >= total:
                return idn_ranges

        element = [batch_size + id_jump + 1, total]
        idn_ranges.append(element)
        return idn_ranges

    @staticmethod
    def _calculate_batch_size(rows: int) -> int:
        threads = os.cpu_count() or 2
        rows /= threads

        if rows < threads:
            return 1

        while rows >= 30000:
            rows /= threads

        return int(rows)