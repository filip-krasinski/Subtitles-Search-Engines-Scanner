import React from 'react';

interface IProps {
    currentPage: number;
    subsPerPage: number;
    totalSubs: number;
    setPage: (page: number) => void;
}

export const Pagination: React.FC<IProps> = ({currentPage, subsPerPage, totalSubs, setPage}) => {
    const pages = Math.floor(totalSubs / subsPerPage) + 1;

    const pageNumbs = getPaginationOptions(subsPerPage, currentPage, pages)

    return (
        <div>
            {pageNumbs.map(n => {
                if (n === currentPage) {
                    return (
                        <button
                            className='pagination-button pagination-button-active'
                            onClick={() => setPage(n)}>
                            {n}
                        </button>
                    )
                } else {
                    return (
                        <button
                            className='pagination-button'
                            onClick={() => setPage(n)}>
                            {n}
                        </button>
                    )
                }
            })}
        </div>
    )


}

const getPaginationOptions = (listLength: number, currentPage: number, totalPages: number) => {
    const offset = Math.ceil(listLength / 2);

    let start = currentPage - offset;
    let end = currentPage + offset;

    if (totalPages <= listLength) {
        start = 0;
        end = totalPages;
    } else if (currentPage <= offset) {
        start = 0;
        end = listLength;
    } else if ((currentPage + offset) >= totalPages) {
        start = totalPages - listLength;
        end = totalPages;
    }

    const out = []
    for (let i = start; i < end; ++i) {
        out.push(i + 1)
    }

    return out;
}